const backwardBtn = document.querySelector(
	".change-controller .changebuttons>div button:nth-child(1)"
);
const playBtn = document.querySelector(
	".change-controller .changebuttons>div button:nth-child(2)"
);
const forwardBtn = document.querySelector(
	".change-controller .changebuttons>div button:nth-child(3)"
);
const imageContainer = document.querySelector(
	".main-con .imageChange .imageChange-con>li>a"
);
const img = document.createElement("img");

img.src = "/img/main/main1.jpg";
img.alt = "album number1";
imageContainer.appendChild(img);

let i = 2;
function toForward() {
	if (i === 5) {
		i = 1;
	}

	img.src = `/img/main/main${i}.jpg`;
	img.alt = "album number" + `${i}`;
	imageContainer.appendChild(img);

	i++;
}

function toBackward() {
	let source = imageContainer.childNodes[0].src;
	console.log(`Current source : ${source}`);
	source = `http://127.0.0.1:5500/img/main/main${i - 1}.jpg`;
	console.log(`Previous source: ${source}`);
}

forwardBtn.addEventListener("click", toForward);
backwardBtn.addEventListener("click", toBackward);
//=======================================================================


const currentPrincipal = document.querySelector('#currentPrincipal');

if(currentPrincipal != null ){
    console.log(`This is the current login name : ${currentPrincipal.value}`);
	localStorage.setItem("currentPrincipal", currentPrincipal.value);


}
