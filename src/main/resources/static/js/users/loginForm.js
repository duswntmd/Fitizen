
    function formCheck(frm) {
    let msg ='';
    if(frm.id.value.length==0) {
    setMessage('id를 입력해주세요.', frm.id);
    return false;
}
    if(frm.pwd.value.length==0) {
    setMessage('password를 입력해주세요.', frm.pwd);
    return false;
}
    return true;
}
    function setMessage(msg, element){
    document.getElementById("msg").innerHTML = ` ${'${msg}'}`;
    if(element) {
    element.select();
}
}