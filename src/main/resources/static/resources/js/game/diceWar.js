var cube = document.getElementsByClassName('cube');
var min = 1;
var max = 24;

function getRandom(max, min) {
  return (Math.floor(Math.random() * (max-min)) + min) * 90;
}

// modulo not giving negative results - see https://stackoverflow.com/q/4467539/1336843
function mod(n, m) {
	return ((n % m) + m) % m;
}

function getResult(rotX, rotY) {
	let countX = mod(rotX / 90, 4);
	if (countX === 1) {
		// Bottom face
		return 6;
	}
	if (countX === 3) {
		// Top face
		return 5;
	}
	// We add countX here to correctly offset in case it is a 180 degrees rotation
	// It can be 0 (no rotation) or 2 (180 degrees)
	let countY = mod(rotY / 90 + countX, 4);
	// Faces order
	return [1, 4, 2, 3][countY];
}
