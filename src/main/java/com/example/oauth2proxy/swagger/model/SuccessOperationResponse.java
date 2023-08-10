package com.example.oauth2proxy.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;

import java.io.Serializable;
import java.util.Objects;

/**
 * Request served successfully, returning result
 */

@Schema(name = "SuccessOperationResponse", description = "Request served successfully, returning result")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SuccessOperationResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private String result;

  public SuccessOperationResponse result(String result) {
    this.result = result;
    return this;
  }

  /**
   * Get result
   * @return result
  */
  
  @Schema(name = "result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("result")
  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SuccessOperationResponse successOperationResponse = (SuccessOperationResponse) o;
    return Objects.equals(this.result, successOperationResponse.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(result);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SuccessOperationResponse {\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

