// 这段代码定义了一个名为 TransformInterceptor 的拦截器，使用了 NestJS 框架。在 NestJS 中，拦截器是一种特殊的中间件，可以在函数处理请求之前或之后执行额外的逻辑。拦截器主要用于执行任务，如日志记录、异常处理、数据转换等。
import {
  CallHandler,
  ExecutionContext,
  Injectable,
  NestInterceptor,
} from '@nestjs/common';
import { Observable, map } from 'rxjs';

// 统一响应结果
@Injectable()
export class TransformInterceptor implements NestInterceptor {
  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    return next.handle().pipe(
      map((data) => ({
        code: 200,
        data,
        message: 'success',
      })),
    );
  }
}
