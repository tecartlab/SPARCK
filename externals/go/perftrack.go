package main

import (
	"sync"

	"github.com/256dpi/max-go"
	"github.com/go-gl/mathgl/mgl64"

	"github.com/256dpi/max-tools/utils"
)

type object struct {
	debug     bool
	inGeo     *max.Inlet
	inLeap    *max.Inlet
	inFilter1 *max.Inlet
	inFilter2 *max.Inlet
	inDebug   *max.Inlet
	outGeo    *max.Outlet
	leap      float64
	filter1   float64
	filter2   float64
	time      float64
	pos       mgl64.Vec3
	rot       mgl64.Quat
	posSpeed  mgl64.Vec3
	rotSpeed  mgl64.Quat
	mutex     sync.Mutex
}

func (o *object) Init(obj *max.Object, args []max.Atom) bool {
	// args: leap, filter1, filter2, debug

	// get leap
	if len(args) > 0 {
		o.leap = utils.Float(args[0])
	}

	// get filter1
	if len(args) > 1 {
		o.filter1 = utils.Float(args[1])
	}

	// get filter1
	if len(args) > 2 {
		o.filter2 = utils.Float(args[2])
	}

	// get debug
	if len(args) > 3 {
		o.debug = utils.Int(args[3]) == 1
	}

	// add inlet and outlets
	o.inGeo = obj.Inlet(max.Any, "t, px, py, pz, rw, rx, ry, rz", true)
	o.inLeap = obj.Inlet(max.Any, "leap", false)
	o.inFilter1 = obj.Inlet(max.Any, "filter1 (input)", false)
	o.inFilter2 = obj.Inlet(max.Any, "filter2 (speed)", false)
	o.inDebug = obj.Inlet(max.Any, "debug", false)
	o.outGeo = obj.Outlet(max.Any, "t, px, py, pz, rw, rx, ry, rz")

	return true
}

func (o *object) Handle(inlet int, _ string, data []max.Atom) {
	// acquire mutex
	o.mutex.Lock()
	defer o.mutex.Unlock()

	// check inlet
	switch inlet {
	case 0:
		// continue
	case 1:
		o.leap = utils.Float(data[0])
		return
	case 2:
		o.filter1 = utils.Float(data[0])
		return
	case 3:
		o.filter2 = utils.Float(data[0])
		return
	case 4:
		o.debug = utils.Int(data[0]) == 1
		return
	default:
		max.Error("invalid inlet")
		return
	}

	// check data
	if len(data) != 8 {
		max.Error("invalid arguments")
		return
	}

	// get time
	time := utils.Float(data[0])

	// get position
	px := utils.Float(data[1])
	py := utils.Float(data[2])
	pz := utils.Float(data[3])
	pos := mgl64.Vec3{px, py, pz}

	// get rotation
	rw := utils.Float(data[4])
	rx := utils.Float(data[5])
	ry := utils.Float(data[6])
	rz := utils.Float(data[7])
	rot := mgl64.Quat{W: rw, V: mgl64.Vec3{rx, ry, rz}}

	// debug
	if o.debug {
		max.Pretty("input", time, pos, rot)
	}

	// set initial values and return
	if o.time == 0 {
		o.time = time
		o.pos = pos
		o.rot = rot
		return
	}

	/* Input Smoothing */

	// smooth position and rotation
	if o.filter1 > 0 {
		pos = utils.LerpVec3(o.pos, pos, o.filter1)
		rot = mgl64.QuatSlerp(o.rot, rot, o.filter1)
	}

	/* Speed */

	// get delta
	delta := time - o.time

	// get position and rotation speed
	posSpeed := pos.Sub(o.pos).Mul(1 / delta)
	rotSpeed := rot.Sub(o.rot).Scale(1 / delta)

	/* Speed Smoothing */

	// smooth speeds
	if o.filter2 > 0 {
		posSpeed = utils.LerpVec3(o.posSpeed, posSpeed, o.filter2)
		rotSpeed = mgl64.QuatLerp(o.rotSpeed, rotSpeed, o.filter2)
	}

	/* Prediction */

	// debug
	if o.debug {
		max.Pretty("delta", delta, posSpeed, rotSpeed)
	}

	// prepare output
	pTime := time
	pPos := pos
	pRot := rot

	// continue if there is positive time difference
	if delta > 0 {
		// predict position and rotation
		pTime = time + o.leap
		pPos = pos.Add(posSpeed.Mul(o.leap))
		pRot = rot.Add(rotSpeed.Scale(o.leap)).Normalize()

		// debug
		if o.debug {
			max.Pretty("pred", pTime, pPos, pRot)
		}
	}

	// send output
	o.outGeo.List([]max.Atom{
		pTime,
		pPos.X(), pPos.Y(), pPos.Z(),
		pRot.W, pRot.X(), pRot.Y(), pRot.Z(),
	})

	// update state
	o.time = time
	o.pos = pos
	o.rot = rot
	o.posSpeed = posSpeed
	o.rotSpeed = rotSpeed
}

func (o *object) Free() {}

func main() {
	max.Register("perftrack", &object{})
}
