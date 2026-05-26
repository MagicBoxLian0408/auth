package kr.magicbox.auth.adapter.out.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import kr.magicbox.auth.adapter.out.jwt.constants.JwtConstants;
import kr.magicbox.auth.adapter.out.jwt.properties.JwtProperties;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.application.port.out.TokenManager;
import kr.magicbox.auth.domain.vo.AccessTokenValue;
import kr.magicbox.auth.domain.vo.RefreshTokenValue;
import kr.magicbox.auth.domain.vo.UserId;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Component
public class JwtTokenManager implements TokenManager {

    private final JwtProperties jwtProperties;

    public JwtTokenManager(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public AccessTokenValue generateAccessToken(UserId userId, UserRole role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        String accessTokenValueString = Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(userId.value().toString())
                .claim(JwtConstants.CLAIM_ROLE, role.name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(jwtProperties.getPrivateKey())
                .compact();

        return AccessTokenValue.of(accessTokenValueString);
    }

    @Override
    public RefreshTokenValue generateRefreshToken(UserId userId, UserRole role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        String refreshTokenValueString = Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(userId.value().toString())
                .claim(JwtConstants.CLAIM_ROLE, role.name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(jwtProperties.getPrivateKey())
                .compact();

        return RefreshTokenValue.of(refreshTokenValueString);
    }

    @Override
    public UserId extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtProperties.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long userId = Long.parseLong(claims.getSubject());
        return UserId.of(userId);
    }

    @Override
    public long getRefreshTokenExpiration() {
        return jwtProperties.getRefreshTokenExpiration();
    }

    @Override
    public UserRole extractRole(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtProperties.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String role = claims.get(JwtConstants.CLAIM_ROLE, String.class);
        return UserRole.of(role);
    }

    public RSAPublicKey getPublicKey() {
        return jwtProperties.getPublicKey();
    }
}
