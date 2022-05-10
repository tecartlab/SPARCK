
// MIT License
// Martin Froehlich for tecartlab.com
// Copyright 2020 - tecartlab.com

uniform float projection_mode;
uniform int stage_mode;

// samplers
uniform sampler2DRect tex0;

uniform vec4 beamer_color[6];

uniform vec3 beamer_dir[6];
uniform vec3 beamer_pos[6];

uniform vec4 offColor; 	    // off color
uniform int back_blend;     // blend background color
uniform int use_bgcolor;    // use background color

uniform float interpolation_correction;

// generated by the vertex shader

varying vec4 beamer_uv[6];		// beamer uv position
varying vec2 beamer_texcoord[6];// beamer texcoord

varying vec3 normal;	// surface normal
varying vec3 worldPos;	// vertex world position

vec4 getTexture2DRect(int index, vec2 coord){
	return texture2DRect(tex0, coord);
}

vec4 getProjectorColor(int i){
	return getTexture2DRect(i, beamer_texcoord[i]);
}

vec4 alphablend(vec4 src, vec4 dst){
	float outA = src.a + dst.a * (1.0 - src.a);
	return vec4((src.rgb * src.a + dst.rgb * dst.a * (1.0 - src.a))/outA, outA);
}

// entry point
void main()
{
	vec3 ray, raynormal;
	vec2 col;
	float angle;
	int i = 0;

	ray = beamer_pos[i] - worldPos;
	raynormal = normalize(ray);

	// calculate the angle between the surface normal and the projected beam.
	// 		rsp. the angle between the surface normal and the projector direction.
	angle = dot(normal, raynormal);

	// sets from which side (back, both, front) it is visible
	angle = (projection_mode == 0.)? 1.:max(sign(angle * projection_mode), 0.);

	// checks if the worldPos is in front of the camera.
	//   the interpolation_correction factor is making sure that fragment
	//   interpolation errors will be cut away, with the negative
	//   sideeffect of less total angle visibility.
	angle = angle * max(sign(dot(beamer_dir[i],raynormal)-interpolation_correction), 0.);

	// calculate the viewport linear box blend
	col = (0.5 - abs(beamer_uv[i].xy - 0.5));

	vec4 fragColor = getProjectorColor(i) * sign(clamp(min(col.x,col.y), 0., 1.)) * angle;

	gl_FragData[0] = alphablend(fragColor, offColor);
}
