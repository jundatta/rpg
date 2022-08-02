#ifdef GL_ES
precision mediump float;
#endif
uniform vec2 resolution;
uniform float time;
uniform vec2 mouse;

const float Pi = 3.14159;

vec3 hsb2rgb(vec3 c ){
    vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0,4.0,2.0), 6.0)-3.0)-1.0, 0.0, 1.0 );
    rgb = rgb*rgb*(3.0-2.0*rgb);
    return c.z * mix(vec3(1.0), rgb, c.y);
}

void main()
{
    vec2 fc = gl_FragCoord.xy;
    vec2 p=(2.0*fc-resolution)/min(resolution.x,resolution.y);
   p= gl_FragCoord.xy / (resolution) ;

    float r = 100.;
    vec3 c = vec3(smoothstep(r*1.01,r, distance(fc, resolution*0.5) ));
    vec3 rc = vec3(1.) - c;

    vec2 m =vec2(0.,time);
    for(int i=1;i<10;i++)
    {
        vec2 newp=p;
        newp.x+=0.6/float(i)*sin(float(i)*p.y+time*0.001/10.0) + m.x/20.0;
        newp.y+=0.6/float(i)*sin(float(i)*p.x+time*0.001/10.0) - m.y/20.0;
        p=newp;
    }
    vec3 col =vec3(0.5*sin(p.x*5.0)+0.5);
    vec3 hsb= hsb2rgb(vec3(0.5*sin(p.x*3.0)+0.5, 1.,1.0) );

    vec3 res = (col + hsb*0.5) * c +rc* col;

    gl_FragColor=vec4( res  * 1.5, 1.0);
}
