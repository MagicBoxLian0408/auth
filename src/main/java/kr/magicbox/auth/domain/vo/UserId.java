package kr.magicbox.auth.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.magicbox.auth.domain.exception.InvalidFieldException;

public record UserId(@JsonValue Long value) {
    
    public UserId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("사용자 ID는 0보다 큰 값이어야 합니다.");
        }
    }
    
    public static UserId of(Long value) {
        return new UserId(value);
    }
}