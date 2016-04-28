package merchant.model;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.admin.domain.AdminRole;
import com.myee.tarot.admin.domain.AdminUserAttribute;
import com.myee.tarot.core.GenericEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_MERCHANT")
public class Merchant extends GenericEntity<Long, Merchant> {

    @Id
    @Column(name = "MERCHANT_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MERCHANT_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
