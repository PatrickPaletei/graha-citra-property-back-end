package com.citra.graha.util

import com.citra.graha.entity.MstUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.xml.bind.DatatypeConverter
import org.slf4j.LoggerFactory
import java.util.*
import javax.crypto.spec.SecretKeySpec

class JWTGenerator {
    companion object {
        private const val SECRET_KEY = "2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566D"
        private val instance: JWTGenerator = JWTGenerator()
    }

    val log = LoggerFactory.getLogger(this::class.java)

    fun createJWT(user: MstUser): String {
        val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256
        val nowMills: Long = System.currentTimeMillis()
        val now = Date(nowMills)
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY)
        val signingKey = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)

        val builder: JwtBuilder = Jwts.builder().setId(user.userId.toString())
            .setIssuedAt(now)
            .setSubject(user.username)
            .claim("user_id", user.userId)
            .setIssuer("grahacitra")
            .setAudience("grahacitra")
            .signWith(signingKey, signatureAlgorithm)
        val expMills = nowMills + 7200000L // 2 jam
        val exp = Date(expMills)
        builder.setExpiration(exp)
        return builder.compact()

    }

    fun decodeJWT(jwt: String): Claims {
        val claims: Claims = Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
            .parseClaimsJws(jwt).body
        log.info("User: ${claims["user_id"]}")
        return claims
    }
}