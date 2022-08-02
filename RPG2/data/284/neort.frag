// https://neort.io/art/bl6seic3p9fafudebpe0

precision highp float;

uniform vec2 resolution;
uniform float time;
uniform vec2 mouse;
uniform sampler2D backbuffer;

const float PI = 3.1415926;
const float PI2 = 6.2831852;

// Archimedean spiral
float spiral2(vec2 m, float b, float w) {
	float r = length(m) * b;
	float a = atan(-m.y, m.x);
	float v = sin(r - a) * w;
    float rv = clamp(v,-1., 1.);
    return rv;
    // return v;
}


float sdCircle( vec2 p, float r )
{
  return length(p) - r;
}

mat2 rotate2d(float t){
    return mat2(
        cos(t), -sin(t),
        sin(t),  cos(t));
}

float spiralMask(vec2 m, float b, float speed)
{
    m = rotate2d(PI +time * speed)*m;
    float a = atan(-m.y, m.x);
	float c = sdCircle(m, 0.875 - time * 0.004 * PI2 * speed + (a/PI2+1.) * 0.1);
    return c;
}

float sdBox( vec3 p, vec3 b )
{
  vec3 d = abs(p) - b;
  return length(max(d,0.0))
         + min(max(d.x,max(d.y,d.z)),0.0); // remove this line for an only partially signed sdf 
}

vec3 hsv(float h, float s, float v){
    vec4 t = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(vec3(h) + t.xyz) * 6.0 - vec3(t.w));
    return v * mix(vec3(t.x), clamp(p - vec3(t.x), 0.0, 1.0), s);
}

vec2 map(vec3 p){
    vec2 p2 = vec2(p.x, p.z);
    float len = length(p2);
    
    float speed = 0.01;
    float t = PI * 12.5 - time * speed;
    vec2 p3 = vec2(t * sin(t), t * cos(t)) / 40.;

    float cir = 1. - smoothstep(0.0, 0.025, sdCircle(p2 - p3, 0.05));
    float c = -spiral2(p2, 40., 2.) * 0.01;

    float rl = spiralMask(p2, 40., speed);

    float b = sdBox(p, vec3(1.1, 0.025, 1.1));
    
    c *= 1. - step(0., rl);
    c += max(0.002, rl);
    c = max(c, b);
    
    return vec2(c, cir);
}

void main(){
    vec2 uv = (gl_FragCoord.xy * 2.0 - resolution) / resolution.y;
    
    vec3 ro=vec3(0., 1.5, 1.5);
 	vec3 ta = vec3(0., -0.3, 0.);

 	vec3 w=normalize(ta-ro);
 	vec3 u=normalize(cross(w,vec3(0,1,0)));
    vec3 rd=mat3(u,cross(u,w),w)*normalize(vec3(uv,2));
    
    vec3 col = vec3(0);
    float d,t=0.0;
    for(float i=1.0;i>0.0;i-=1.0/80.0)
    {
        vec2 d2 = map(ro+t*rd);
     	t+=d2.x;
        if(d2.x<0.001)
        {
            col = hsv(0.3, 1., 0.4) * i*i*i;
            vec3 fire = hsv(0.05, 1., 1.) * d2.y;
            col = mix(col, fire, d2.y);
            break;
        }
    }
    col = mix(vec3(0.1), col, exp(-t*t*1e-4));
    gl_FragColor = vec4(col, 1.0);
}
