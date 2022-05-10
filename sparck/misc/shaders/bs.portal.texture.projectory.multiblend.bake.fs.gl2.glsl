
// MIT License
// Martin Froehlich for tecartlab.com
// Copyright 2020 - tecartlab.com

#version 120
#extension GL_ARB_texture_rectangle : enable

/*
	Multifunctional texture projection shader with full feature set.
*/

uniform float projection_mode;
uniform int stage_mode;

// samplers
uniform sampler2DRect tex0;
uniform sampler2DRect tex1;
uniform sampler2DRect tex2;
uniform sampler2DRect tex3;
uniform sampler2DRect tex4;
uniform sampler2DRect tex5;
uniform sampler2DRect tex6;

uniform int beamer_count;
uniform vec4 beamer_color[6];

uniform vec3 beamer_pos[6];
uniform vec3 beamer_dir[6];

uniform float bevel_size[6];
uniform float bevel_curve[6];
uniform int bevel_round[6];

uniform vec4 offColor; 	// off color
uniform int back_blend;     // blend background color
uniform int use_bgcolor;    // use background color

uniform float distance_influence;
uniform float blend_spread;
uniform float angle_mode;

uniform float angle_limit_low[6];
uniform float angle_limit_high[6];

uniform float far_clip[6];

uniform float interpolation_correction;

// generated by the vertex shader

varying vec4 beamer_uv[6];		// beamer uv position
varying vec2 beamer_texcoord[6];// beamer texcoord
varying vec2 texcoord6;         // default texcoord

varying vec3 normal;	// surface normal
varying vec3 worldPos;	// vertex world position

float blendSpread = pow(((0.5 + (blend_spread)*10))*2., 4.);

// constants

const float PI = 3.1415926535897932384626433832795;
const float PI_HALF = PI / 2.0;
const vec4 WHITE = vec4( 1.0, 1.0, 1.0, 1.0);
const vec4 BLACK = vec4( 0.0, 0.0, 0.0, 1.0);

vec4 getTexture2DRect(int index, vec2 coord){
	return
        (index == 0)?texture2DRect(tex0, coord):
        (index == 1)?texture2DRect(tex1, coord):
        (index == 2)?texture2DRect(tex2, coord):
        (index == 3)?texture2DRect(tex3, coord):
        (index == 4)?texture2DRect(tex4, coord):texture2DRect(tex5, coord);
}

vec4 getTextureColor(int i){
	return getTexture2DRect(i, beamer_texcoord[i]);
}

vec4 alphablend(vec4 src, vec4 dst){
	float outA = src.a + dst.a * (1.0 - src.a);
	return vec4((src.rgb * src.a + dst.rgb * dst.a * (1.0 - src.a))/outA, outA);
}

// entry point
void main()
{
	float	vcurve[6];
	float 	vangle[6];
	float 	spreadedAngle[6];

	int   	indexSort[6];

	vec3 	ray, raynormal;
	vec2 	col;
	float 	curve, angle, linearCurve, powerCurve, visible, visible2, veepee, distance;
    int 	i, j;

  	for( i = 0; i < 6; i++){
		vcurve[i] = 0.0;
		vangle[i] = 0.0;
		spreadedAngle[i] = 0.0;
		indexSort[i] = i;
	}
    
    //Calculating the factor of importance for each beamer
	for( i = 0; i < beamer_count; i++){
        //textureColor[i] = getTextureColor(i);
		// calculate the viewray from the camera to this fragment
		ray = beamer_pos[i] - worldPos;
		raynormal = normalize(ray);

		// calculate the angle between the surface normal and the projected beam.
		// 		rsp. the angle between the surface normal and the projector direction.
		angle = dot(normal, raynormal * (1.0 - angle_mode) + beamer_dir[i] * angle_mode);

		// checks if the worldPos is in front of the camera.
		//   the interpolation_correction factor is making sure that fragment
		//   interpolation errors will be cut away, with the negative
		//   sideeffect of less total angle visibility.
		angle = angle * max(sign(dot(beamer_dir[i],raynormal)-interpolation_correction), 0.);

		// sets from which side (back, both, front) it is visible
		angle = (projection_mode == 0.)? abs(angle):angle * projection_mode;

		// it calculates the relation between the distance and the far clip
		angle *= min(1.0, 1.0 - distance_influence * length(ray) / far_clip[i]);

		//calculates the fadeout factor for the angle;
		visible = smoothstep(angle_limit_low[i], angle_limit_high[i], angle);

        //and apply alpha value of this texture
        visible = visible * getTextureColor(i).a;

		// calculate the viewport linear box blend
		col = (0.5 - abs(beamer_uv[i].xy - 0.5)) * (20. - bevel_size[i] * 18.0);
		col = clamp(col, 0.0, 1.0);

		// transform the box blend into a chanfer box
		linearCurve = (bevel_round[i] == 1)?1.0 - clamp(sqrt(pow(1.0-col.y,2.0) + pow(1.0-col.x,2.0)), 0.0, 1.0):clamp(min(col.x,col.y), 0., 1.);

		veepee = sign(linearCurve);

		// transform the linear blend into an s-shaped blend
		float powFactor = 1.0 + abs(bevel_curve[i] * 5.0);
		powerCurve = linearCurve * linearCurve * (3. - 2. * linearCurve);
		powerCurve = (bevel_curve[i] > 0.)?1.0 - pow(1.0 - powerCurve, powFactor):pow(powerCurve, powFactor);

		vcurve[i] = powerCurve * visible;
		vangle[i] = angle * visible * veepee;
	}

	//Sorting the viewport values
	int idxmax;

    for(i = (beamer_count - 1); i > 0; --i){
    	for(j = 0; j < i; j++){
	    	idxmax = (vangle[indexSort[j]] > vangle[indexSort[j+1]])? indexSort[j]: indexSort[j + 1];
    		indexSort[j + 1] = indexSort[j] + indexSort[j + 1] - idxmax;
    		indexSort[j] = idxmax;
    	}
    }

	//spread the angles and calc the blend factor for each of the four possible beams
	spreadedAngle[indexSort[0]] = vcurve[indexSort[0]] * vangle[indexSort[0]];
	spreadedAngle[indexSort[1]] = vcurve[indexSort[1]] * vangle[indexSort[1]] * pow((1. - vangle[indexSort[0]] + vangle[indexSort[1]]), blendSpread);
	spreadedAngle[indexSort[2]] = vcurve[indexSort[2]] * vangle[indexSort[2]] * pow((1. - vangle[indexSort[0]] + vangle[indexSort[2]]), blendSpread);
	spreadedAngle[indexSort[3]] = vcurve[indexSort[3]] * vangle[indexSort[3]] * pow((1. - vangle[indexSort[0]] + vangle[indexSort[3]]), blendSpread);

	float sumAngle = 0.00001 + spreadedAngle[indexSort[0]] + spreadedAngle[indexSort[1]] + spreadedAngle[indexSort[2]] + spreadedAngle[indexSort[3]];

	// normalizing the blend factors for the first time
	// and multiply it with the curve.
	float normalizeOne = 1.0 / sumAngle;
	spreadedAngle[indexSort[0]] *= normalizeOne;
	spreadedAngle[indexSort[1]] *= normalizeOne;
	spreadedAngle[indexSort[2]] *= normalizeOne;
	spreadedAngle[indexSort[3]] *= normalizeOne;

	float sumCurve = vcurve[indexSort[0]] + vcurve[indexSort[1]] + vcurve[indexSort[2]] + vcurve[indexSort[3]];

	//absolute blend factor, used to blend in the background color
	//    make sure it is not bigger than 1.
	float blendRef = min(1.0,sumCurve + (1. - back_blend)) * sign(sumAngle);

    vec4 color = getTextureColor(indexSort[0]) * spreadedAngle[indexSort[0]];
    color += getTextureColor(indexSort[1]) * spreadedAngle[indexSort[1]];
    color += getTextureColor(indexSort[2]) * spreadedAngle[indexSort[2]];
    color += getTextureColor(indexSort[3]) * spreadedAngle[indexSort[3]];
    color = vec4(color.rgb, color.a * (1. - back_blend) + blendRef * (back_blend));

    // create gbcolor - either taking it from the background texture or the flat color
    vec4 bgColor = texture2DRect(tex6, texcoord6) * (1. - use_bgcolor) + offColor * use_bgcolor;

    gl_FragData[0] = alphablend(color, bgColor);
}
