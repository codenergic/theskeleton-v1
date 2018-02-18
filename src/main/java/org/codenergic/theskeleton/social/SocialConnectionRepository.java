/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenergic.theskeleton.social;

import org.codenergic.theskeleton.core.data.AuditingEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SocialConnectionRepository extends AuditingEntityRepository<SocialConnectionEntity> {
	List<SocialConnectionEntity> findByProviderAndProviderUserId(String provider, String providerUserId);

	List<SocialConnectionEntity> findByProviderAndProviderUserIdIn(String provider, Collection<String> providerUserIds);

	List<SocialConnectionEntity> findByUserIdAndProvider(String userId, String provider);

	SocialConnectionEntity findByUserIdAndProviderAndProviderUserId(String userId, String provider, String providerUserId);

	List<SocialConnectionEntity> findByUserIdAndProviderAndRank(String userId, String provider, int rank);

	List<SocialConnectionEntity> findByUserIdAndProviderOrderByRankAsc(String userId, String provider);

	List<SocialConnectionEntity> findByUserIdOrderByRankAsc(String userId);

	@Query("SELECT COALESCE(MAX(c.rank) + 1, 1) "
			+ "FROM SocialConnectionEntity c "
			+ "WHERE c.providerUserId = ?1 AND c.provider = ?2")
	int getRank(String userId, String provider);
}