

// 현재 시간 보여주기
const currentTime = document.querySelector('.currentTime');


showCurrentTime();
function showCurrentTime() {
    const current = new Date();

    let hours = current.getHours().toString().padStart(2, '0');
    let mins = current.getMinutes().toString().padStart(2, '0');
    let secs = current.getSeconds().toString().padStart(2, '0');

    currentTime.innerHTML = `${hours}:${mins}:${secs}`;
}

(() => {
    setInterval(showCurrentTime, 1000);
})()


// 왼쪽 메뉴 버튼 클릭시 메뉴 옵션들 toggle 
const leftMenuBtns = document.querySelectorAll('.leftNavBarCon .leftNavBarCon-lists>*>*:nth-child(1)');
const leftMenuOptionsList = document.querySelectorAll('.leftNavBarCon-lists>*>*:nth-child(2)');
const hideLeftNavContents = 'hideLeftNavContents';
const currentMenuCon = document.querySelectorAll('.leftNavBarCon .leftNavBarCon-lists>*');

// 해당 버튼을 클릭하고, 다른 곳을 클릭했을 때, 열려있는 메뉴를 다시 사라지게 하기
document.querySelector('body').addEventListener('click', function(e) {
    leftMenuBtns.forEach((el, index)=>{
        if(e.target.className == e.currentTarget.querySelector(`.${el.className}`).className){
            leftMenuOptionsList[index].classList.remove(hideLeftNavContents);
        } else {
            leftMenuOptionsList[index].classList.add(hideLeftNavContents);
        }
    })
});



// 상단 메뉴 버튼 클릭 시, 왼쪽 메뉴 옵션들 toggle
const topMenuBtns = document.querySelectorAll('.li-con .left-menu>ul>li');
function showLeftMenuOptionsTopBtns(i) {
    const topBtn = i.currentTarget;

    // const index = Array.prototype.indexOf.call(topMenuBtns, topBtn);

    for (let j = 0; j < topMenuBtns.length; j++) {
        if (topBtn == topMenuBtns[j]) {
            leftMenuOptionsList[j].classList.toggle(hideLeftNavContents);
            console.dir(`This is topBtn : ${topBtn}`);
        } else {
            leftMenuOptionsList[j].classList.add(hideLeftNavContents);
        }
    }
};


topMenuBtns.forEach((el) => {
    el.addEventListener('click', showLeftMenuOptionsTopBtns);
})



// 상단 메뉴 버튼 위에 mouseenter시, 왼쪽 메뉴 옵션 보여줌
const topMenuOptions = document.querySelectorAll('.li-con .left-menu>ul>li>*:nth-child(2)');

function showHoverPoint(e){
    let topMenuTarget = e.currentTarget;
    let currentTopMenuIndex = Array.prototype.indexOf.call(topMenuBtns, topMenuTarget);
    console.dir(currentTopMenuIndex);
    
    topMenuOptions.forEach((el, index)=>{
        if(currentTopMenuIndex == index){
            el.classList.toggle(hideLeftNavContents);
        } else {
            el.classList.add(hideLeftNavContents);
        }
    });
}
topMenuBtns.forEach((el)=>{
    el.addEventListener('mouseenter', showHoverPoint);
})

// 상단 메뉴 mouseleave 하면, 왼쪽 메뉴 옵션 사라짐
topMenuBtns.forEach((el)=>{
    el.addEventListener('mouseleave', ()=>{
        topMenuOptions.forEach((e)=>{
            e.classList.add(hideLeftNavContents);
        })
    });
});