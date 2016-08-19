package org.stepik.plugin.modules;

public class TokenInfo {
    public String refresh_token;
    public String token_type;
    public String access_token;
    public String scope;
    public int expires_in;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenInfo tokenInfo = (TokenInfo) o;

        if (expires_in != tokenInfo.expires_in) return false;
        if (refresh_token != null ? !refresh_token.equals(tokenInfo.refresh_token) : tokenInfo.refresh_token != null)
            return false;
        if (token_type != null ? !token_type.equals(tokenInfo.token_type) : tokenInfo.token_type != null) return false;
        if (access_token != null ? !access_token.equals(tokenInfo.access_token) : tokenInfo.access_token != null)
            return false;
        return scope != null ? scope.equals(tokenInfo.scope) : tokenInfo.scope == null;

    }

    @Override
    public int hashCode() {
        int result = refresh_token != null ? refresh_token.hashCode() : 0;
        result = 31 * result + (token_type != null ? token_type.hashCode() : 0);
        result = 31 * result + (access_token != null ? access_token.hashCode() : 0);
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        result = 31 * result + expires_in;
        return result;
    }
}
