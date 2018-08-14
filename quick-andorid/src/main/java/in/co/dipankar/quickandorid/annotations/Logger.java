package in.co.dipankar.quickandorid.annotations;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import in.co.dipankar.quickandorid.utils.DLog;

@Aspect
//@Component
public class Logger
{

    @Around("execution(* *(..)) && @annotation(in.co.dipankar.quickandorid.annotations.MethodStats)")
    public Object log(ProceedingJoinPoint point) throws Throwable
    {
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        DLog.d("MethodStat# "+MethodSignature.class.cast(point.getSignature()).getDeclaringTypeName()+"::"+
                        MethodSignature.class.cast(point.getSignature()).getMethod().getName()+" -> Time: "+
                (System.currentTimeMillis() - start)+"ms");
        return result;
    }
}