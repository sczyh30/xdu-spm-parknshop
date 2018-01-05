package io.spm.parknshop.apply.domain;

import java.util.List;

/**
 * @author Eric Zhao
 */
public class ApplyVO {

  private Long applyId;
  private Apply apply;
  private List<ApplyRecordVO> records;

  private Object bizData;

  public Long getApplyId() {
    return applyId;
  }

  public ApplyVO setApplyId(Long applyId) {
    this.applyId = applyId;
    return this;
  }

  public Apply getApply() {
    return apply;
  }

  public ApplyVO setApply(Apply apply) {
    this.apply = apply;
    return this;
  }

  public List<ApplyRecordVO> getRecords() {
    return records;
  }

  public ApplyVO setRecords(List<ApplyRecordVO> records) {
    this.records = records;
    return this;
  }

  public Object getBizData() {
    return bizData;
  }

  public ApplyVO setBizData(Object bizData) {
    this.bizData = bizData;
    return this;
  }
}
