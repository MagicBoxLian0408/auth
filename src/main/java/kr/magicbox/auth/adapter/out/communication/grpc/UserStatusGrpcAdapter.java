package kr.magicbox.auth.adapter.out.communication.grpc;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.ManagedChannel;
import kr.magicbox.auth.adapter.out.communication.grpc.exception.UserServiceUnavailableException;
import kr.magicbox.auth.application.port.out.UserStatusPort;
import kr.magicbox.auth.grpc.user.CheckUserActiveRequest;
import kr.magicbox.auth.grpc.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStatusGrpcAdapter implements UserStatusPort {

    private final ManagedChannel userManagedChannel;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "checkUserActiveFallback")
    @TimeLimiter(name = "userService", fallbackMethod = "checkUserActiveFallback")
    public CompletableFuture<Boolean> isActive(Long userId) {
        return GrpcFutures.toCompletable(
                UserServiceGrpc.newFutureStub(userManagedChannel).checkUserActive(
                        CheckUserActiveRequest.newBuilder().setUserId(userId).build()
                )
        ).thenApply(response -> response.getActive());
    }

    @SuppressWarnings("unused")
    private CompletableFuture<Boolean> checkUserActiveFallback(Long userId, Throwable throwable) {
        log.warn("User 서비스 연결 실패: {}", throwable.getMessage());
        throw new UserServiceUnavailableException(throwable);
    }
}
