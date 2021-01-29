// MIT License
// Martin Froehlich for tecartlab.com
// Copyright 2020 - tecartlab.com

#version 330 core

/*
	Multifunctional texture projection shader with full feature set.
*/

layout (location = 0) out vec4 outColor0;

uniform float projection_mode;
uniform int stage_mode;
uniform int mode;

// samplers
uniform samplerJit0 tex0;
uniform samplerJit1 tex1;
uniform samplerJit2 tex2;
uniform samplerJit3 tex3;
uniform samplerJit4 tex4;
uniform samplerJit5 tex5;

uniform mat4 textureMatrix0;
uniform mat4 textureMatrix1;
uniform mat4 textureMatrix2;
uniform mat4 textureMatrix3;
uniform mat4 textureMatrix4;
uniform mat4 textureMatrix5;

uniform int beamer_count;
uniform vec4 beamer_color[6];

uniform mat4 beamer_v_matrix[6];

uniform vec3 beamer_pos[6];
uniform vec3 beamer_dir[6];

uniform float bevel_size[6];
uniform float bevel_curve[6];
uniform int bevel_round[6];

uniform vec4 offColor; 	// off color

uniform float distance_influence;
uniform float back_blend;
uniform float blend_spread;
uniform float angle_mode;

uniform float angle_limit_low[6];
uniform float angle_limit_high[6];

uniform float far_clip[6];

uniform float interpolation_correction;

// generated by the vertex shader
in jit_PerVertex {
    vec4 jit_texcoord0;
    vec4 beamer_uv[6];
    vec2 beamer_texcoord[6];
    float depth[6];
    vec3 normal;
    vec3 worldPos;
} jit_in;

// constants

const float PI = 3.1415926535897932384626433832795;
const float PI_HALF = PI / 2.0;
const vec4 WHITE = vec4( 1.0, 1.0, 1.0, 1.0);
const vec4 BLACK = vec4( 0.0, 0.0, 0.0, 1.0);

// entry point
void main()
{
    vec2    proj_texcoord[6];
    vec4    proj_texUV[6];
	float	vcurve[6];

    mat4    rot_matrix;
	vec3 	ray, raynormal, viewline, vl_abs;
	vec2 	col;
	float 	curve, angle, linearCurve, powerCurve, visible, visible2, veepee, distancem, radius, theta, phi;

    vcurve[0] = 0.0;

    // calculate the viewray from the camera to this fragment
    ray = beamer_pos[0] - jit_in.worldPos;
    raynormal = normalize(ray);

    // calculate the angle between the surface normal and the projected beam.
    // 		rsp. the angle between the surface normal and the projector direction.
    angle = dot(jit_in.normal, raynormal);

    // sets from which side (back, both, front) it is visible
    angle = (projection_mode == 0.)? 1.:max(sign(angle * projection_mode), 0.);

    // calculate rotation matrix from beamer view matrix - its not accurate and cause problem if
    // the matrix is not scale = 1 1 1, but its a quick and functional fix that should not be a heavy
    // performance hit.
    rot_matrix = beamer_v_matrix[0];
    rot_matrix[3] = vec4(0., 0., 0., 1.); // the positional infos are removed.

    viewline = (rot_matrix * vec4(-ray, 1)).xyz;
    vl_abs = abs(viewline);

    // calculate the spherical coordinates
    radius = length(viewline);
    theta = atan(viewline.x, -viewline.z);
    phi = atan(sqrt(viewline.z*viewline.z+viewline.x*viewline.x),viewline.y);

    // calculate the equirectangular coordinates
    proj_texUV[0] = vec4((theta + PI) / (2 * PI), 1.0 - phi / PI, 0., 1.);
    proj_texcoord[0] = vec2(textureMatrix0 * proj_texUV[0]);

    // check if we are still inside the farclip
    vcurve[0] = (far_clip[0] > radius)?angle: 0.;

	outColor0 = (vcurve[0] > 0)?texture(tex0, proj_texcoord[0]):offColor;
    
}
