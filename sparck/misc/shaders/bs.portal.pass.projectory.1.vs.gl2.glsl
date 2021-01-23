/* MIT License
 *
 * Copyright (c) 2012-2020 tecartlab.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author maybites
 *
 */

/* Vertex Shader
 *
 * default 1 texture projection vertex shader used for texture projections
 */

#version 120

uniform mat4 viewport_matrix;
uniform mat4 world_matrix;
uniform mat4 collada_factor;

uniform mat4 beamer_v_matrix[6];
uniform mat4 beamer_p_matrix[6];

uniform float far_clip[1];

varying vec4 beamer_uv[1];		// beamer uv position
varying vec2 beamer_texcoord[1];// beamer texcoord

varying float depth[1];// beamer distance

varying vec3 normal;	// surface normal
varying vec3 worldPos;	// vertex world position

mat4 screen_m_matrix = world_matrix * collada_factor;

mat4 mv0_matrix = beamer_v_matrix[0] * screen_m_matrix;

mat4 mvp0_matrix = beamer_p_matrix[0] * mv0_matrix;

void main(void)
{
    // transform texcoords
    //texcoord0 = vec2(gl_TextureMatrix[0] * gl_MultiTexCoord0);

	// transform vertex space to worldspace: used for projectory
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

	normal = normalize(mat3(screen_m_matrix) * gl_Normal);
	worldPos = vec3(screen_m_matrix * gl_Vertex);

	vec4 clip, device, screen;
    vec4 position;
    
	// calculate the distance to beamer 0
	position = mv0_matrix * gl_Vertex;
    depth[0] = length(position.xyz) / far_clip[0];
	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp0_matrix * gl_Vertex);
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	beamer_uv[0] = vec4(screen.x, screen.y, 0., 1.);
	beamer_texcoord[0] = vec2(gl_TextureMatrix[0] * beamer_uv[0]);
}
