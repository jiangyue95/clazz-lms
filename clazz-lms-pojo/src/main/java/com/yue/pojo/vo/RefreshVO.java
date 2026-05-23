package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response body for POST /refresh.
 *
 * <p>Returns only a new access token. The refresh token itself is NOT
 * rotated - clients continue using the same refresh token util it
 * natural expiry (7 days). Token rotation is a deferred follow-up
 * per design phase Trade-off 5.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshVO {

    private String accessToken;
}
