const track = document.querySelector(".track");
const artist = document.querySelector(".artist");
const introduction1 = document.querySelector(".introduction1");
const introduction2 = document.querySelector(".introduction2");
const on = "on";
const shadow = "active";

function trackFn() {
	if (
		!introduction2.classList.contains(on) &&
		introduction1.classList.contains(on)
	) {
		introduction1.classList.remove(on);
		introduction2.classList.add(on);
	}
}

function artistFn() {
	if (
		!introduction1.classList.contains(on) &&
		introduction2.classList.contains(on)
	) {
		introduction1.classList.add(on);
		introduction2.classList.remove(on);
	}
}

track.addEventListener("click", trackFn);
artist.addEventListener("click", artistFn);

const detailMiddle = document.querySelector(".detail-middle");
const class1 = document.querySelectorAll(".class1");

detailMiddle.addEventListener("click", (e) => {
	class1.forEach((el, idx) => {
		if (e.target == el.children[0]) {
			el.children[0].classList.add(shadow);
		} else {
			el.children[0].classList.remove(shadow);
		}
	});
});

Array.from(document.querySelectorAll(".material-ripple")).forEach((a) => {
	a.addEventListener("click", function (e) {
		const ripple = document.createElement("div"),
			rect = a.getBoundingClientRect();
		(ripple.className = "animate"),
			(ripple.style.left = `${e.x - rect.left}px`),
			(ripple.style.top = `${e.y - rect.top}px`),
			(ripple.style.background = `#${
				a.dataset.color !== undefined ? a.dataset.color : "bdc3c7"
			}`),
			ripple.style.setProperty("--material-scale", a.offsetWidth),
			a.append(ripple),
			setTimeout(function () {
				ripple.parentNode.removeChild(ripple);
			}, 500);
	});
});
let userCart =  document.querySelector("#userCart");
userCart.addEventListener("click",()=>{
    alert("장바구니에 등록 되었습니다.")
});