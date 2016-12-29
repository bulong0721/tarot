package com.myee.tarot.configuration.domain;

/**
 * Created by Ray.Fu on 2016/12/28.
 */

import com.myee.tarot.core.GenericEntity;
import javax.persistence.*;

@Entity
@Table(name = "C_RECEIPT_PRODUCT_USED_XREF")
public class ReceiptProductUsedXref extends GenericEntity<Long, ReceiptProductUsedXref> {

    @Id
    @Column(name = "RECEIPT_PRODUCT_USED_XREF_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "RECEIPT_PRODUCT_USED_XREF_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "RECEIPT_ID")
    private Long receiptId;

    @Column(name = "PRODUCT_USED_ID")
    private Long productUsedId;

    @Column(name = "TYPE")
    private String type;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getProductUsedId() {
        return productUsedId;
    }

    public void setProductUsedId(Long productUsedId) {
        this.productUsedId = productUsedId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
