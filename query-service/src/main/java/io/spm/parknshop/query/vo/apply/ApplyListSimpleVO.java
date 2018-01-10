package io.spm.parknshop.query.vo.apply;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyRecordVO;

public class ApplyListSimpleVO {

  private Apply apply;
  private ApplyRecordVO newRecord;

  public ApplyListSimpleVO() {
  }

  public ApplyListSimpleVO(Apply apply, ApplyRecordVO newRecord) {
    this.apply = apply;
    this.newRecord = newRecord;
  }

  public Apply getApply() {
    return apply;
  }

  public ApplyListSimpleVO setApply(Apply apply) {
    this.apply = apply;
    return this;
  }

  public ApplyRecordVO getNewRecord() {
    return newRecord;
  }

  public ApplyListSimpleVO setNewRecord(ApplyRecordVO newRecord) {
    this.newRecord = newRecord;
    return this;
  }
}
