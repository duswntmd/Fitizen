from fastapi import FastAPI, HTTPException, File, UploadFile, Request
from fastapi.middleware.cors import CORSMiddleware
from tensorflow.keras.models import load_model
from fastapi.staticfiles import StaticFiles
from collections import deque
from pydantic import BaseModel
import mediapipe as mp #이거 오류
import tempfile
import shutil
import openai
import numpy as np
import ollama  
import tensorflow as tf
import cv2
import os
import pandas as pd
import joblib
import re
import requests
from chat_functions import *
from fastapi.responses import JSONResponse
from fastapi.exceptions import RequestValidationError
from langchain_openai import ChatOpenAI   # instruct 모델은 OpenAI
from dotenv import load_dotenv
from langchain.document_loaders import PyPDFLoader
from langchain.text_splitter import CharacterTextSplitter
from langchain.embeddings import OpenAIEmbeddings
from langchain.vectorstores import FAISS
load_dotenv()

app= FastAPI()
app
openai.api_key = os.getenv("OPENAI_API_KEY")
llm = ChatOpenAI(model="gpt-4o-mini")
llm

LOCAL_DIRECTORY = os.path.join(os.path.dirname(__file__), "fitizenmovie")
app.mount("/videos", StaticFiles(directory=LOCAL_DIRECTORY), name="videos")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 모든 도메인에서의 요청을 허용하려면 "*" 사용
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 서버 상태 확인을 위한 기본 엔드포인트
@app.get("/")
def read_root():
    print("VECTOR_STORE_FILE 경로:", VECTOR_STORE_FILE)
    print("벡터 스토어 존재 여부:", os.path.exists(VECTOR_STORE_FILE))
    return {"message": "Welcome to ChatGPT FastAPI!"}

class VideoRequest(BaseModel):
    video_url: str

loaded_lstm_model = load_model('datakeras\\sqaut_model_checkpoint2.keras')  # 모델 경로 설정

@app.post("/analyzevideo")
async def analyze_video(request: VideoRequest):
    try:
        print("Step 1: Starting analyze_video function")  # 시작 확인
        # URL로부터 비디오 스트림 가져오기
        try:
            response = requests.get(request.video_url, stream=True,  verify=False)
            response.raise_for_status()
            print("Step 2: Video stream successfully retrieved")  # 비디오 요청 성공
        except requests.exceptions.RequestException as e:
            print(f"Step 2 Error: {str(e)}")
            raise HTTPException(status_code=500, detail=f"비디오 URL 요청 오류: {str(e)}")
        
        # 임시 파일에 비디오 저장
        try:
            with tempfile.NamedTemporaryFile(delete=False, suffix=".mp4") as tmp_file:
                tmp_path = tmp_file.name
                for chunk in response.iter_content(chunk_size=1024):
                    if chunk:
                        tmp_file.write(chunk)
            print("Step 3: Video saved to temporary file")  # 임시 파일 저장 성공
        except Exception as e:
            print(f"Step 3 Error: {str(e)}")
            raise HTTPException(status_code=500, detail=f"임시 파일 저장 중 오류 발생: {str(e)}")

        # 비디오 파일을 열어서 프레임별로 처리
        try:
            cap = cv2.VideoCapture(tmp_path)
            print("Downloaded file size:", os.path.getsize(tmp_path))
            if not cap.isOpened():
                print("Step 4 Error: 비디오 파일을 열 수 없습니다.")
                raise HTTPException(status_code=500, detail="비디오 파일을 열 수 없습니다.")
            print("Step 4: Video file opened successfully")  # 비디오 파일 열기 성공
        except Exception as e:
            print(f"Step 4 Error: {str(e)}")
            raise HTTPException(status_code=500, detail=f"비디오 파일 열기 오류: {str(e)}")

        # 모델 로드 (경로 확인)
        try:
            
            loaded_lstm_model
            print('모델 로드 완료')
            print("Step 5: Model loaded successfully")  # 모델 로딩 성공
        except Exception as e:
            print(f"Step 5 Error: {str(e)}")
            raise HTTPException(status_code=500, detail=f"LSTM 모델 로드 오류: {str(e)}")
        
        try:
            mp_drawing = mp.solutions.drawing_utils
            mp_pose = mp.solutions.pose
            pose = mp_pose.Pose(min_detection_confidence=0.5, min_tracking_confidence=0.5)
            print("Step 6: Mediapipe pose initialized")  # mediapipe 초기화 성공
        except Exception as e:
            print(f"Step 6 Error: {str(e)}")
            raise HTTPException(status_code=500, detail=f"mediapipe pose 초기화 오류: {str(e)}")

         # 분석된 비디오를 저장할 경로 설정
        w = round(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        h = round(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        fps = cap.get(cv2.CAP_PROP_FPS)
        # 'H264' 또는 'avc1' Ai서버로 올릴시 openh264-1.8.0-win64.dll 다운후 파이썬스크립트에 적용
        fourcc = cv2.VideoWriter_fourcc(*'H264')  
        output_video_path = 'fitizenmovie/processed_' + os.path.basename(tmp_path)
        out = cv2.VideoWriter(output_video_path, fourcc, fps, (w, h))

        frame_cnt = 0
        sequence_dq = deque(maxlen=30)
        analysis_results = []
        bone_idx = [11, 12, 13, 14, 15, 16, 23, 24, 25, 25, 27, 28]  # 관절 인덱스

        # 비디오 분석 루프
        try:
            while cap.isOpened():
                ret, frame = cap.read()
                if not ret:
                    break  
                frame_cnt += 1
                print(f"Step 7: Processing frame {frame_cnt}")  # 프레임 처리 중

                # 이미지 전처리 및 포즈 추출
                image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                image.flags.writeable = False
                results = pose.process(image)
                image.flags.writeable = True
                image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
                mp_drawing.draw_landmarks(image, results.pose_landmarks, mp_pose.POSE_CONNECTIONS)  # 랜드마크 정보를 사용하여 연결선을 그림
                out.write(image)

                if results.pose_landmarks is None:
                    print(f"No landmarks detected for frame {frame_cnt}")
                    analysis_results.append({"frame": frame_cnt, "prediction": "No landmarks"})
                    continue

                # 관절 위치 데이터 수집
                row = []
                for idx in bone_idx:
                    row.extend([round(results.pose_landmarks.landmark[idx].x, 5),
                                round(results.pose_landmarks.landmark[idx].y, 5)])
                sequence_dq.append(row)

                # 시퀀스 길이가 충분히 쌓였을 때 예측 수행
                if len(sequence_dq) == 30:
                    pred = loaded_lstm_model.predict(np.array(list(sequence_dq)).reshape(1, 30, 24))
                    pred_value = pred[0].argmax()

                    # 자세별 예측 결과 매핑
                    # pose_name = ["정자세", "무릎 안좋은 자세", "앉은 정도", "허리", "알 수 없는 자세"][pred_value]
                    # analysis_results.append({"frame": frame_cnt, "prediction": pose_name})
                    pose_code = str(pred_value)
                    analysis_results.append(f"{frame_cnt}:{pose_code}")
        except Exception as e:
            print(f"Step 7 Error: {str(e)}")
            raise HTTPException(status_code=500, detail=f"비디오 분석 오류: {str(e)}")

        cap.release()
        out.release()
        pose.close()

        # 임시 파일 삭제
        if os.path.exists(tmp_path):
            os.remove(tmp_path)
            print("Step 8: Temporary file deleted")  # 임시 파일 삭제 성공

        # 분석 결과 반환
        print("Step 9: Returning analysis results")  # 결과 반환
        
        ai_server_url = f"{os.path.basename(output_video_path)}"
        print("Step 10: Returning video URL")
        
        # return {"frame_count": frame_cnt, "analysis_results": analysis_results}
        analysis_result_str = ",".join(analysis_results)
        aianswer = await analyze_video_ask_chatgpt(analysis_result_str)
        print(f"ChatGPT AI Answer: {aianswer}")
        
        return {
            "analysis_results": analysis_result_str,
            "aivideourl": ai_server_url,
            "aianswer": aianswer.get("answer", "응답 없음")
        }

    except Exception as e:
        print(f"알 수 없는 오류 발생: {str(e)}")  # 전체적인 예외 처리
        raise HTTPException(status_code=500, detail=f"알 수 없는 오류 발생: {str(e)}")

async def analyze_video_ask_chatgpt(question_str: str):
    try:
        # 받은 질문을 출력
        print("Received question:", question_str)

        # 벡터 스토어 초기화
        vector_store = get_or_create_vector_store()
        print("Vector store initialized.")

        # 관련 문서 검색
        relevant_docs = search_relevant_docs(vector_store, question_str)
        print(f"Found {len(relevant_docs)} relevant documents.")

        # 관련 문서를 하나의 텍스트로 합치기
        relevant_texts = " ".join([doc.page_content for doc in relevant_docs])

        # GPT 프롬프트 생성
        gpt_prompt = (
            f"다음 스쿼트 자세 프레임 데이터를 분석하고 피드백을 제공하세요.\n\n"
            f"프레임 데이터: {question_str}\n\n"
            "자세 값:\n"
            "  0: 올바른 자세.\n"
            "  1: 무릎 문제 (예: 무릎이 안쪽으로 움직이거나, 발끝을 초과함).\n"
            "  2: 깊이 문제 (예: 너무 얕거나 너무 깊음).\n"
            "  3: 허리 각도 문제 (예: 둥글거나 과도하게 펴짐).\n\n"
            "규칙:\n"
            "- 반드시 한국어로만 작성하세요. 영어로 응답하지 마세요.\n"
            "- 제공된 프레임 데이터에 없는 자세 값에 대해서는 피드백을 작성하지 마세요.\n"
            "- 특정 자세 값이 많은 경우 해당 문제에 대한 피드백에 집중하세요.\n"
            "- 여러 자세 값이 섞여 있는 경우, 각 유형별로 제공된 프레임 데이터에 있는 자세 값에 대해 분석하고 피드백을 작성하세요.\n"
            "- 프레임 데이터 갯수를 정확하게 작성하세요.\n"
            "- 문제를 요약하고, 해결 방법을 제안하세요.\n\n"
            "분석 결과를 아래에 한국어로만 작성하세요:"
        )
        print("Generated GPT Prompt:", gpt_prompt)

        # GPT 모델 호출
        gpt_response = llm.invoke(gpt_prompt)
        print("GPT response received.")

        # 응답에서 내용을 추출
        answer = gpt_response.content
        print("Extracted answer:", answer)

        # 결과 반환
        return {"answer": answer}
    except Exception as e:
        print(f"Error in analyze_video_ask_chatgpt: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

   


# 전처리기 및 모델 로드
preprocessor = joblib.load("preprocessor.pkl")
model = tf.keras.models.load_model("exercise_recommendation_model.keras")

# 운동 클래스 라벨
class_labels = ["badminton", "basketball", "cardio", "health", "pilates", "swimming", "tabletennis", "yoga"]


# 입력 데이터 형식을 정의하는 Pydantic 모델
class ExerciseInput(BaseModel):
    height: int
    weight: int
    goal: str
    frequency: str
    preference: str

# 예측 엔드포인트
@app.post("/predict_exercise")
async def predict_exercise(input_data: ExerciseInput):
    # 입력 데이터를 DataFrame으로 변환
    input_df = pd.DataFrame([input_data.dict()])
    print("Received input data:", input_data)
    
    # 전처리
    try:
        processed_data = preprocessor.transform(input_df)
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Data preprocessing error: {e}")

    # 모델 예측 수행
    try:
        predictions = model.predict(processed_data)[0]  # 첫 번째 예측 결과만 사용
        top_indices = np.argsort(predictions)[-3:][::-1]  # 상위 3개 인덱스를 높은 순으로 정렬

        # 추천된 운동과 정확도 추출
        recommended_exercises = [class_labels[i] for i in top_indices]
        confidence_scores = [predictions[i] for i in top_indices]
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Model prediction error: {e}")

    # 예측 결과와 정확도 반환
    return {
 "recommended_exercise1": recommended_exercises[0],
        "recommended_exercise2": recommended_exercises[1],
        "recommended_exercise3": recommended_exercises[2],
        "confidence_score1": round(float(confidence_scores[0]*100), 2),
        "confidence_score2": round(float(confidence_scores[1]*100), 2),
        "confidence_score3": round(float(confidence_scores[2]*100), 2)
    }


# 요청 바디 모델 정의
class QuestionRequest(BaseModel):
    question: str

VECTOR_STORE_FILE = "vector_store.faiss"
PDF_FILE = "test.pdf"

# 벡터 스토어 생성 함수
def create_vector_store_from_pdf(pdf_file):
    try:
        print(f"Loading PDF file: {pdf_file}")
        loader = PyPDFLoader(pdf_file)
        documents = loader.load()
        print(f"Loaded {len(documents)} documents from PDF.")
    except Exception as e:
        print(f"Error while loading PDF file: {e}")
        raise HTTPException(status_code=500, detail=f"PDF 파일을 로드할 수 없습니다: {e}")

    try:
        print("Splitting documents into chunks...")
        text_splitter = CharacterTextSplitter(chunk_size=500, chunk_overlap=0)
        docs = text_splitter.split_documents(documents)
        print(f"Split into {len(docs)} document chunks.")
    except Exception as e:
        print(f"Error while splitting documents: {e}")
        raise HTTPException(status_code=500, detail=f"문서 청크 처리 중 오류 발생: {e}")

    try:
        print("Creating embeddings...")
        embeddings = OpenAIEmbeddings()
        vector_store = FAISS.from_documents(docs, embeddings)
        print("Vector store created successfully.")
        return vector_store
    except Exception as e:
        print(f"Error while creating vector store: {e}")
        raise HTTPException(status_code=500, detail=f"벡터 스토어 생성 중 오류 발생: {e}")


# 벡터 스토어 저장 함수
def save_vector_store(vector_store, file_path):
    vector_store.save_local(file_path)

# 벡터 스토어 로드 함수
def load_vector_store(file_path):
    embeddings = OpenAIEmbeddings()
    return FAISS.load_local(file_path, embeddings, allow_dangerous_deserialization=True)

# 사용자의 질문(query)과 유사한 문서를 검색
def search_relevant_docs(vector_store, query):
    embeddings = OpenAIEmbeddings()
    query_embedding = embeddings.embed_query(query)
    results = vector_store.similarity_search_by_vector(query_embedding, k=3)
    return results

# ChatGPT API 호출 함수 (검색된 문서에 기반한 답변 생성)
def call_chatgpt_api(prompt):
    response = openai.Completion.create(
        model="gpt-4o-mini",  # 사용할 GPT 모델
        prompt=prompt,
        max_tokens=100
    )
    return response.choices[0].text.strip()
def get_or_create_vector_store():
    if os.path.exists(VECTOR_STORE_FILE):
        print("저장된 벡터 스토어를 로드합니다...")
        return load_vector_store(VECTOR_STORE_FILE)
    else:
        print("벡터 스토어가 없습니다. PDF에서 생성 중...")
        vector_store = create_vector_store_from_pdf(PDF_FILE)
        save_vector_store(vector_store, VECTOR_STORE_FILE)
        print("벡터 스토어가 생성되고 저장되었습니다.")
        return vector_store    

# 질문을 받아서 OpenAI API에 요청하는 엔드포인트
@app.post("/ask")
async def ask_chatgpt(request: Request):
    try:
        
        body = await request.json()
        raw_body = await request.body()  # 요청 본문 출력
        print("Raw body:", raw_body)
    except Exception as e:
        raise HTTPException(status_code=400, detail="Invalid JSON provided")
    
    question = body.get("question")
    if not question:
        raise HTTPException(status_code=400, detail="No question provided")
    
    print("Received question:", question)

    print("Step 1: Initializing vector store...")
    vector_store = get_or_create_vector_store()
    print("Step 1 Complete: Vector store initialized successfully.")
    print(f"Step 2: Searching relevant documents for question: {question}")
    relevant_docs = search_relevant_docs(vector_store, question)
    print(f"Step 2 Complete: Found {len(relevant_docs)} relevant documents.")
    print("Step 3: Combining relevant documents into a single text...")
    relevant_texts = " ".join([doc.page_content for doc in relevant_docs])
    print(f"Step 3 Complete: Combined text length: {len(relevant_texts)}")
    print("Step 4: Creating GPT prompt...")
    gpt_prompt = (
    f"Based on the following documents, answer the question: {question}\n\nDocuments:\n{relevant_texts}\n\n"
    "If the question is not related to the content of the document, "
    "please respond with: 'I'm unable to provide an answer.'\n"
    "If there is a URL in the response, display it on the last line."
    )
    print(f"Step 4 Complete: GPT prompt created successfully:\n{gpt_prompt}")
    print("Step 5: Invoking GPT model...")
    gpt_response = llm.invoke(gpt_prompt)
    print(f"Step 5 Complete: GPT response received:\n{gpt_response}")
    print("Step 6: Extracting content from GPT response...")
    answer = gpt_response.content  # content 부분만 추출
    print(f"Step 6 Complete: Extracted answer:\n{answer}")
    print("ChatGPT 응답:", answer)
    
    try:
        return {"answer": answer}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request, exc):
    print("Validation error:", exc)
    return JSONResponse(
        status_code=422,
        content={"detail": exc.errors(), "body": exc.body},
    )

# 올라마 API 요청 엔드포인트
@app.post("/ask2")
async def ask_ollama(request: QuestionRequest):
    question = request.question
    if not question:
        raise HTTPException(status_code=400, detail="No question provided")

    # 벡터 스토어 로드 및 관련 문서 검색
    #vector_store = get_or_create_vector_store()
    #relevant_docs = search_relevant_docs(vector_store, question)
    #relevant_texts = " ".join([doc.page_content for doc in relevant_docs])
    
    # Ollama에 전달할 프롬프트 생성
    # ollama_prompt = f"Based on the following documents, answer the question: {question}\n\nDocuments:\n{relevant_texts}If there is a URL in the response, display it on the last line. "
    ollama_prompt = f"당신은 FITIZEN 웹사이트의 정보와 운동루틴, 운동 식단에 대해 잘 알고 있는 전문가이다:{question}라는 질문에 잘 답해줘"
    try:
        # Ollama API에 프롬프트 전달
        response = ollama.chat(
            #mistral  ,llama-3-Korean-Bllossom-8B-Q4_K_M.gguf
            model="unsloth.Q8_0.gguf",
            messages=[{"role": "user", "content": ollama_prompt}]
        )
        print(response)
        
        # 응답에서 답변 추출
        answer = response['message']['content']
        return {"answer": answer}
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


#ssl_certfile="server.crt"
# FastAPI 앱 실행을 위한 코드
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8000, ssl_certfile="fullchain.pem", ssl_keyfile="rootCA.key")
