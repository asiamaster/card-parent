package com.dili.tcc.common;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 15:02
 */
public class TransactionId {
    /**全局id*/
    private String gid;
    /**分支事务id*/
    private Set<String> cids = new LinkedHashSet<>();

    public TransactionId(String gid) {
        this.gid = gid;
    }

    public void addCid(String cid) {
        this.cids.add(cid);
    }

    public String getGid() {
        return gid;
    }

    public Set<String> getCids() {
        return cids;
    }

}
