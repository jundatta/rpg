precision highp float;
uniform float time;
uniform vec2 resolution;

uniform float uniformAspect;	// resolution.x / resolution.y
uniform float uniformInverseAspect;	// 1.0 / (resolution.x / resolution.y)

// params
// MODE : 0,1,2
#define MODE 1
#define SLIDE_SPAN 8.0
#define ITR 8
#define SCALE 1.2
#define COLOR_SMOOTH_MIN 0.1
#define COLOR_SMOOTH_MAX 0.4

const float PI = acos(-1.);

vec3 hsv(in float h, in float s, in float v) { return ((clamp(abs(fract(h+vec3(0., 2., 1.)/3.0)*6.-3.)-1., 0., 1.)-1.)*s+1.)*v; }

float easeIn(in float t) { return pow(t, 8.); }
float easeOut(in float t) { return 1.0-easeIn(1.0-t); }
float easeInOut(in float t) { return t<0.5 ? 0.5*easeIn(t*2.0) : 0.5+0.5*easeOut(t*2.0-1.0); }

mat2 rotate(in float r) { float c=cos(r), s=sin(r); return mat2(c, -s, s, c); }
vec2 rotate(in vec2 p, in float r) { return rotate(r) * p; }
vec3 rotate(in vec3 p, in vec3 r) {
  vec3 q = p;
  q.xy = rotate(q.xy, r.z);
  q.yz = rotate(q.yz, r.x);
  q.zx = rotate(q.zx, r.y);
  return q;
}

float hash(in float x) { return fract(sin(x) * 43237.5324); }
float hash(in vec2 x) { return fract(sin(dot(x, vec2(12.9898, 78.233)))*43237.5324); }
vec3 hash3(in float x) { return vec3(hash(x), hash(x+100.), hash(10000.)); }
float noise(in vec2 p) {
  vec2 f = fract(p);
  vec2 i = floor(p);
  vec2 u = f*f*(3.-2.*f);
  return mix(
      mix(hash(i+vec2(0., 0.)), hash(i+vec2(1., 0.)), u.x),
      mix(hash(i+vec2(0., 1.)), hash(i+vec2(1., 1.)), u.x),
      u.y
    );
}
float noise(in vec3 p) {
  vec3 f = fract(p);
  vec3 i = floor(p);
  vec3 u = f*f*(3.-2.*f);
  float n = i.x + i.y*53.0 + i.z*117.0;
  return mix(
      mix(mix(hash(n+0.), hash(n+1.), u.x), mix(hash(n+53.0), hash(n+54.0), u.x), u.y),
      mix(mix(hash(n+117.), hash(n+118.), u.x), mix(hash(n+170.0), hash(n+171.0), u.x), u.y),
      u.z
    );
}
float fbm(in vec3 p) {
  float res = 0.;
  res += 0.5000*noise(p); p = rotate(p*2.02, vec3(PI*0.125));
  res += 0.2500*noise(p); p = rotate(p*2.03, vec3(PI*0.125));
  res += 0.1250*noise(p); p = rotate(p*2.01, vec3(PI*0.125));
  res += 0.0625*noise(p);
  return res;
}

void main()
{
    vec2 uv = gl_FragCoord.xy / resolution.xy;	// 0.0 <= uv <= 1.0かにゃ？
    
    float t = time*(1./SLIDE_SPAN);
    uv.x += floor(t) + easeInOut(fract(t));
    vec2 fuv = fract(uv)*2.0-1.0;	// -1.0 < fuv < 1.0 であってる？
    fuv *= uniformInverseAspect;	// 逆数（もとに戻す）？
    vec2 iuv = floor(uv);	// 色が変わる！！（とりあえず円の大きさには無関係(^_^)）
    
    // render
    vec3 col = vec3(1.);
    vec3 v = vec3(fuv + iuv, 0.);
    for(int i=0;i<ITR;i++) {
        float fi = float(i);
        float f = fbm(v);
        vec2 vxy = MODE==0 ? v.xy*SCALE : (MODE==1 ? v.xy*SCALE+f : v.xy*SCALE*f);
    	v = vec3(vxy, fi+f+time*0.1);
        vec3 c = hsv(iuv.x*0.3+f+0.02*fi, 1.0-f*hash(iuv), 1.);
        col = mix(c, col, smoothstep(COLOR_SMOOTH_MIN, COLOR_SMOOTH_MAX, f));
    }
    
    // frame
    uv = abs(fuv*vec2(uniformAspect, 1.));
    float r=0.9, w=0.01;
    vec3 frameCol = vec3(0.98);	// 淡い白？
    col = mix(frameCol, col, smoothstep(r, r-w, length(uv)));
    col = mix(frameCol, col, smoothstep(r, r-w, max(uv.x, uv.y)));

    // Output to screen
    gl_FragColor = vec4(col,1.0);
}