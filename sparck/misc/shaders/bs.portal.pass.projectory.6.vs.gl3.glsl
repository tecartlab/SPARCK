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
 * default 6 texture projection vertex shader used for texture projections
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

uniform int beamer_count;
uniform mat4 beamer_v_matrix[6];
uniform mat4 beamer_p_matrix[6];

uniform float far_clip[6];

// vertex
out jit_PerVertex {
    vec4 jit_texcoord0;
    vec4 beamer_uv[6];
    vec2 beamer_texcoord[6];
    float depth[6];
    vec3 normal;
    vec3 worldPos;
} jit_out;

mat4 screen_m_matrix = world_matrix * collada_factor;

mat4 mv0_matrix = beamer_v_matrix[0] * screen_m_matrix;
mat4 mv1_matrix = beamer_v_matrix[1] * screen_m_matrix;
mat4 mv2_matrix = beamer_v_matrix[2] * screen_m_matrix;
mat4 mv3_matrix = beamer_v_matrix[3] * screen_m_matrix;
mat4 mv4_matrix = beamer_v_matrix[4] * screen_m_matrix;
mat4 mv5_matrix = beamer_v_matrix[5] * screen_m_matrix;

mat4 mvp0_matrix = beamer_p_matrix[0] * mv0_matrix;
mat4 mvp1_matrix = beamer_p_matrix[1] * mv1_matrix;
mat4 mvp2_matrix = beamer_p_matrix[2] * mv2_matrix;
mat4 mvp3_matrix = beamer_p_matrix[3] * mv3_matrix;
mat4 mvp4_matrix = beamer_p_matrix[4] * mv4_matrix;
mat4 mvp5_matrix = beamer_p_matrix[5] * mv5_matrix;

mat4 mvp_matrix(int index){
    return (index == 0)?mvp0_matrix:(index == 1)?mvp1_matrix:(index == 2)?mvp2_matrix:(index == 3)?mvp3_matrix:(index == 4)?mvp4_matrix:mvp5_matrix;
}

mat4 mv_matrix(int index){
    return (index == 0)?mv0_matrix:(index == 1)?mv1_matrix:(index == 2)?mv2_matrix:(index == 3)?mv3_matrix:(index == 4)?mv4_matrix:mv5_matrix;
}

mat4 texMatrix(int index){
    return (index == 0)?textureMatrix0:(index == 1)?textureMatrix1:(index == 2)?textureMatrix2:(index == 3)?textureMatrix3:(index == 4)?textureMatrix4:textureMatrix5;
}

void main(void)
{
    // transform texcoords
    jit_out.jit_texcoord0 = textureMatrix0 * vec4(jit_texcoord, 0., 1.);

	// transform vertex sapce to worldspace: used for projectory
	gl_Position = modelViewProjectionMatrix * vec4(jit_position, 1);

	jit_out.normal = normalize(mat3(screen_m_matrix) * jit_normal);
	jit_out.worldPos = vec3(screen_m_matrix * vec4(jit_position, 1));

	vec4 clip, device, screen, position;

    for(int i = 0; i < beamer_count; i++){ 
        // calculate the distance to beamer 0
        position = mv_matrix(i) * vec4(jit_position, 1);
        jit_out.depth[i] = length(position.xyz) / far_clip[i];
        // calculate the UV-coordinate for beamer 0
        clip = vec4(mvp_matrix(i) * vec4(jit_position, 1));
        device = vec4(clip.x / clip.w, clip.y / clip.w, clip.z / clip.w, 1.);
        screen = viewport_matrix * device;
        jit_out.beamer_uv[i] = vec4(screen.x, screen.y, 0., 1.);
        jit_out.beamer_texcoord[i] = vec2(texMatrix(i) * jit_out.beamer_uv[i]);
    }
    
}
