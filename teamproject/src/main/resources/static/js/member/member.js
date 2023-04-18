  $('#emailChecked').on('click',emailCheckedFn);
  function emailCheckedFn(){
         const data={
             'email':$('#email').val()
                  };
        $.ajax({
             type:'post', // 방식
              url:'/emailChecked', //url
               data: data,
               success:function(res){ //성공
                      if(res==1){
                        alert("이름사용 가능")
                      }else{
                        alert("이름사용 불가")
                      }
                 },
                error:function(){ // 실패
                console.log('실패');
                    }
            })
        }