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

#version 330 core

in vec3 jit_position;
in vec2 jit_texcoord;
in vec3 jit_normal;

uniform mat4 textureMatrix0;
uniform mat4 textureMatrix1;
uniform mat4 textureMatrix2;
uniform mat4 textureMatrix3;
uniform mat4 textureMatrix4;
uniform mat4 textureMatrix5;
uniform mat4 modelViewProjectionMatrix;

uniform mat4 viewport_matrix;
uniform mat4 world_matrix;
uniform mat4 collada_factor;

uniform mat4 beamer_v_matrix[6];
uniform mat4 beamer_p_matrix[6];

mat4 screen_m_matrix = world_matrix * collada_factor;

mat4 mvp0_matrix = beamer_p_matrix[0] * beamer_v_matrix[0] * screen_m_matrix;
mat4 mvp1_matrix = beamer_p_matrix[1] * beamer_v_matrix[1] * screen_m_matrix;
mat4 mvp2_matrix = beamer_p_matrix[2] * beamer_v_matrix[2] * screen_m_matrix;
mat4 mvp3_matrix = beamer_p_matrix[3] * beamer_v_matrix[3] * screen_m_matrix;
mat4 mvp4_matrix = beamer_p_matrix[4] * beamer_v_matrix[4] * screen_m_matrix;
mat4 mvp5_matrix = beamer_p_matrix[5] * beamer_v_matrix[5] * screen_m_matrix;

// vertex
out jit_PerVertex {
    vec4 jit_texcoord0;
    vec4 beamer_uv[6];
    vec2 beamer_texcoord[6];
    vec3 normal;
    vec3 worldPos;
} jit_out;

void main(void)
{
  // transform texcoords
  jit_out.jit_texcoord0 = textureMatrix0 * vec4(jit_texcoord, 0., 1.);

	// perform required transform on vertex
	// gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
  gl_Position = vec4(vec2(jit_texcoord).xy * 2. - 1., 0., 1.);

	jit_out.normal = normalize(mat3(screen_m_matrix) * jit_normal);
	jit_out.worldPos = vec3(screen_m_matrix * vec4(jit_position, 1));

	vec4 clip, device, screen;

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp0_matrix * vec4(jit_position, 1));
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	jit_out.beamer_uv[0] = vec4(screen.x, screen.y, 0., 1.);
	jit_out.beamer_texcoord[0] = vec2(textureMatrix0 * jit_out.beamer_uv[0]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp1_matrix * vec4(jit_position, 1));
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	jit_out.beamer_uv[1] = vec4(screen.x, screen.y, 0., 1.);
	jit_out.beamer_texcoord[1] = vec2(textureMatrix1 * jit_out.beamer_uv[1]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp2_matrix * vec4(jit_position, 1));
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	jit_out.beamer_uv[2] = vec4(screen.x, screen.y, 0., 1.);
	jit_out.beamer_texcoord[2] = vec2(textureMatrix2 * jit_out.beamer_uv[2]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp3_matrix * vec4(jit_position, 1));
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	jit_out.beamer_uv[3] = vec4(screen.x, screen.y, 0., 1.);
	jit_out.beamer_texcoord[3] = vec2(textureMatrix3 * jit_out.beamer_uv[3]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp4_matrix * vec4(jit_position, 1));
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	jit_out.beamer_uv[4] = vec4(screen.x, screen.y, 0., 1.);
	jit_out.beamer_texcoord[4] = vec2(textureMatrix4 * jit_out.beamer_uv[4]);

	// calculate the UV-coordinate for beamer 0
	clip = vec4(mvp5_matrix * vec4(jit_position, 1));
	device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
	screen = viewport_matrix * device;
	jit_out.beamer_uv[5] = vec4(screen.x, screen.y, 0., 1.);
	jit_out.beamer_texcoord[5] = vec2(textureMatrix5 * jit_out.beamer_uv[5]);
}
