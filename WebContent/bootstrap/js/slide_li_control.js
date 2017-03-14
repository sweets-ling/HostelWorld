///**
// * 通过session存储当前的 active的编号
// * 
// * 在点击li的时候，对sesion进行赋值
// * 
// * 可以不用获取，自动获取active的值
// * @param data
// */
//function setActiveLi(data1, data2, data3) {
//
//	$.session.set('activeL1', data1);
//	$.session.set('activeL2', data2);
//	$.session.set('activeL3', data3);
//
//}
//
///**
// * 
// */
//function getActiveLi() {
//	// 每次加载的时候，执行这个方法
//	var data1 = $.session.get('activeL1');
//	if(data1!=null){
//		
//		
//		
//		
//		
//	}
//
//}

// 		$(document).ready(function() {
// 			$('#side-menu li').each(function() {
// 				$(this).click(function() {
// 					alert($(this).attr('id'));  //触发两次，第一次是里面的li，第二次是上一层的li
// 				});		
// 				$.session.set('activeL1', data1);
// 				$.session.set('activeL2', data2);
// 				$.session.set('activeL3', data3);
// 			});
// 		});