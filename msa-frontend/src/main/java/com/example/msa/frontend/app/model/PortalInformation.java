package com.example.msa.frontend.app.model;

import java.io.Serializable;
import com.example.msa.common.model.UserResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PortalInformation implements Serializable {

  private static final long serialVersionUID = 1L;

  UserResource userResource;
}
