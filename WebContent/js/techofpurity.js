 
/**
 * 
 */
///获取未未出翻译数

function getUnAnswerNum()
{
	$.get("../question/getUnAnswerNum",function(data){
		  
		   if(data["num"]>0)
			   $("#unanswernum").text(data["num"]);
		  });
	}

$(function(){ 
setInterval(getUnAnswerNum,61000); 
}); 