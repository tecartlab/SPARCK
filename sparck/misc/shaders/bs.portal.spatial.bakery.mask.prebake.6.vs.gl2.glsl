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

#version 120

varying vec2 texcoord0;

uniform mat4 viewport_matrix;
uniform mat4 world_matrix;
uniform mat4 collada_factor;

uniform mat4 beamer_v_matrix[6];
uniform mat4 beamer_p_matrix[6];

varying vec4 beamer_uv[6];		// beamer uv position
varying vec2 beamer_texcoord[6];// beamer texcoord

varying vec3 normal;	// surface normal
varying vec3 worldPos;	// vertex world position

mat4 screen_m_matrix = world_matrix * collada_factor;

mat4 mvp0_matrix = beamer_p_matrix[0] * beamer_v_matrix[0] * screen_m_matrix;
mat4 mvp1_matrix = beamer_p_matrix[1] * beamer_v_matrix[1] * screen_m_matrix;
mat4 mvp2_matrix = beamer_p_matrix[2] * beamer_v_matrix[2] * screen_m_matrix;
mat4 mvp3_matrix = beamer_p_matrix[3] * beamer_v_matrix[3] * screen_m_matrix;
mat4 mvp4_matrix = beamer_p_matrix[4] * beamer_v_matrix[4] * screen_m_matrix;
mat4 mvp5_matrix = beamer_p_matrix[5] * beamer_v_matrix[5] * screen_m_matrix;

void main(void)
{
    // transform texcoords
    texcoord0 = vec2(gl_TextureMatrix[0] * gl_MultiTexCoord0);

	// perform required transform on vertex
	// gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_Position = vec4(vec2(gl_MultiTexCoord0).xy * 2. - 1., 0., 1.);

	normal = normalize(mat3(screen_m_matrix) * gl_Normal);
	worldPos = vec3(screen_m_matrix * gl_Vertex);

	vec4 clip, device, screen;

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp0_matrix * gl_Vertex);
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	beamer_uv[0] = vec4(screen.x, screen.y, 0., 1.);
	beamer_texcoord[0] = vec2(gl_TextureMatrix[0] * beamer_uv[0]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp1_matrix * gl_Vertex);
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	beamer_uv[1] = vec4(screen.x, screen.y, 0., 1.);
	beamer_texcoord[1] = vec2(gl_TextureMatrix[1] * beamer_uv[1]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp2_matrix * gl_Vertex);
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	beamer_uv[2] = vec4(screen.x, screen.y, 0., 1.);
	beamer_texcoord[2] = vec2(gl_TextureMatrix[2] * beamer_uv[2]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp3_matrix * gl_Vertex);
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	beamer_uv[3] = vec4(screen.x, screen.y, 0., 1.);
	beamer_texcoord[3] = vec2(gl_TextureMatrix[3] * beamer_uv[3]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp4_matrix * gl_Vertex);
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	beamer_uv[4] = vec4(screen.x, screen.y, 0., 1.);
	beamer_texcoord[4] = vec2(gl_TextureMatrix[4] * beamer_uv[4]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp5_matrix * gl_Vertex);
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	beamer_uv[5] = vec4(screen.x, screen.y, 0., 1.);
	beamer_texcoord[5] = vec2(gl_TextureMatrix[5] * beamer_uv[5]);

}
