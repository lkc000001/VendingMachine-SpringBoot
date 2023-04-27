$(function(){

    // 權限視窗控制
    var Competence = $("#Competence");
    $(".member").on("click",function(){
        Competence.slideToggle()
    })
    
    var memberBlock = $(".member-block");// 排除的區塊
    $(document).click(function (e) {
        if (!memberBlock.is(e.target) && memberBlock.has(e.target).length === 0) {
        Competence.slideUp(); 
        }
    });

    // textarea輸入框 輸入內容超過高度時自動往下延展(搭配下方含式)
    //ps:也可直接於該textarea中增加 onkeyup="autogrow(this)"
    $("textarea").on("keyup",function(){
        autogrow(this);
    })
    //  textarea輸入框 輸入內容超過高度時自動往下延展
    function autogrow(textarea){
        var adjustedHeight=textarea.clientHeight;
    
        adjustedHeight=Math.max(textarea.scrollHeight,adjustedHeight);
        if (adjustedHeight>textarea.clientHeight){
            textarea.style.height=adjustedHeight+'px';
        }
    
    }
});

function queryajax(apiurl, methodType, requestData) {
	return $.ajax({
		    type: methodType,
		    url: apiurl,
		    dataType: "json",
		    contentType:"application/json; charset=utf-8",
		    data: JSON.stringify(requestData),
		    cache:false,
		    success: function (data){
				$('#itemsCountDiv').css("display", "block");
				$('#itemsCount').text(data.itemsCount);
		    },
		    error: function (error) {
				console.log(error);
				$("#jsGrid").children().remove();
				$('#itemsCountDiv').css("display", "none");
				showMsg("錯誤", error.responseJSON.errorMessage);
		   	}
		});
}

function objectifyForm(formArray) {
	var returnArray = {};
	for (var i = 0; i < formArray.length; i++){
		returnArray[formArray[i]['name']] = formArray[i]['value'];
	}
	return returnArray;
}

function showMsg(title, msg) {
	$('#myModalLabel').text(title);
	$('#myModalBoby').text(msg);
	$('#myModal').modal('show');
}

function deleteConfirmMsg(title, msg) {
	$('#deleteMsgTitle').text(title);
	$('#deleteMsgBoby').text(msg);
	$('#deleteModal').modal('show');
}

var selectEnabled = [
	{ id: "0", name: "未啟用" },
	{ id: "1", name: "啟用" }
];	    

//清除查詢資料
function doClear() {
	doClearData()
	$('#itemsCountDiv').css("display", "none");
	$("#jsGrid").children().remove();
}

String.prototype.toDate = function(format) {
	var normalized      = this.replace(/[^a-zA-Z0-9]/g, '-');
	var normalizedFormat= format.toLowerCase().replace(/[^a-zA-Z0-9]/g, '-');
	var formatItems     = normalizedFormat.split('-');
	var dateItems       = normalized.split('-');

	var monthIndex  = formatItems.indexOf("mm");
	var dayIndex    = formatItems.indexOf("dd");
	var yearIndex   = formatItems.indexOf("yyyy");
	var hourIndex     = formatItems.indexOf("hh");
	var minutesIndex  = formatItems.indexOf("ii");
	var secondsIndex  = formatItems.indexOf("ss");

	var today = new Date();

	var year  = yearIndex>-1  ? dateItems[yearIndex]    : today.getFullYear();
	var month = monthIndex>-1 ? dateItems[monthIndex]-1 : today.getMonth()-1;
	var day   = dayIndex>-1   ? dateItems[dayIndex]     : today.getDate();

	var hour    = hourIndex>-1      ? dateItems[hourIndex]    : today.getHours();
	var minute  = minutesIndex>-1   ? dateItems[minutesIndex] : today.getMinutes();
	var second  = secondsIndex>-1   ? dateItems[secondsIndex] : today.getSeconds();

	return new Date(year,month,day,hour,minute,second);
};
			