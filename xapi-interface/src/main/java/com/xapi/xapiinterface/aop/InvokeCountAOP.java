package com.xapi.xapiinterface.aop;

/**
 * 每个接口在调用成功之后都需要执行invokeCount的操作，通过AOP将调用次数加1的操作提取出来
 * AOP的缺点：它是独立于单个项目的，每个项目都需要自己实现统计逻辑，并引入相应的AOP包
 */
public class InvokeCountAOP {
}
