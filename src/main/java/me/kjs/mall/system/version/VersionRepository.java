package me.kjs.mall.system.version;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<AppVersion, String> {
}
