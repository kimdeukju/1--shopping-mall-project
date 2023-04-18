const magnifier=document.querySelector('.s-img');
const leftMenu=document.querySelector('.header .header-con .gnb .main-menu-con>ul>li .li-con .left-menu');
const rightSearchBar=document.querySelector('.header .header-con .gnb .main-menu-con>ul>li .li-con .right-menu')
const hide2='hide2';
const show='show';

function showSearchBar(e) {
    e.preventDefault();

    console.dir(e);

    if(rightSearchBar.classList.contains(hide2)){
        rightSearchBar.classList.remove(hide2);
        rightSearchBar.classList.add(show);
        leftMenu.classList.add(hide2);
        leftMenu.classList.remove(show);
    } else {
        leftMenu.classList.remove(hide2);
        leftMenu.classList.add(show);
        rightSearchBar.classList.remove(show)
        rightSearchBar.classList.add(hide2);
    }
}


magnifier.addEventListener('click', showSearchBar);


//==========================================================
//로그아웃 시, localStorage에 있는 로그인 정보 삭제

if(document.getElementById('logout')){
    document.querySelector('#logout').addEventListener('click', ()=>{
            localStorage.removeItem("currentPrincipal");
    })
}

//// 창 종료시 저장되어 있는 로그인 정보 삭제
//
//if(localStorage.length != 0){
//    window.addEventListener('unload', ()=>{
//        localStorage.clear();
//    })
//}