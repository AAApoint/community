$(function () {
    $("#topBtn").click(setTop);
    $("#wonderfulBtn").click(setWonderful);
    $("#deleteBtn").click(setDelete);
});

function like(btn, entityType, entityId, entityUserId, postId) {
    $.post(
        "http://localhost:8080/community/like",
        {"entityType":entityType, "entityId":entityId, "entityUserId":entityUserId, "postId":postId},
        function (data) {
             data = $.parseJSON(data);
             if(data.code == 0){
                 $(btn).children("i").text(data.likeNum);
                 $(btn).children("b").text(data.likeStatus==1?'已赞':'赞');
             }else{
                 alert(data.msg);
             }
        }
    );
}

// 置頂
function setTop() {
    $.post(
        "http://localhost:8080/community/discuss/top",
        {"id":$("#postId").val()},
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 0){
                $("#topBtn").attr("disabled", "disabled");
            }else{
                alert(data.msg);
            }
        }
    );
}

function setWonderful() {
    $.post(
        "http://localhost:8080/community/discuss/wonderful",
        {"id":$("#postId").val()},
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 0){
                $("#wonderfulBtn").attr("disabled", "disabled");
            }else{
                alert(data.msg);
            }
        }
    );
}

function setDelete() {
    $.post(
        "http://localhost:8080/community/discuss/delete",
        {"id":$("#postId").val()},
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 0){
                location.href = "http://localhost:8080/community/index";
            }else{
                alert(data.msg);
            }
        }
    );
}