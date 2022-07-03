package com.example.msa.frontend.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msa.common.apinfra.exception.BusinessException;
import com.example.msa.common.model.UserResource;
import com.example.msa.frontend.domain.repository.UserResourceRepository;

/**
 * Webアプリケーションのビジネスロジックのトランザクション境界となるクラス。<br>
 * バックエンドのマイクロサービスの呼び出しが複数になる場合などに実行フローを制御する役割を持つ。<br>
 * 必要な対象分のマイクロサービスの呼び出しや、リトライ制御、エラーが発生してロールバックしたい際の補償トランザクション処理などもこのクラスで実施する。
 */
@Service
public class OrchestrationServiceImpl implements OrchestrationService {

  @Autowired UserResourceRepository userResourceRepository;

  @Override
  public UserResource getUserResource(String loginId) throws BusinessException {
    return userResourceRepository.findByLoginId(loginId);
  }
}
