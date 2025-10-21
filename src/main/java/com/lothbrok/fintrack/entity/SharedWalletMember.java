package com.lothbrok.fintrack.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.lothbrok.fintrack.entity.base.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shared_wallet_members", indexes = {
        @Index(name = "idx_wallet_member", columnList = "wallet_id,member_id", unique = true)
})
public class SharedWalletMember extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private SharedWallet wallet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Column(name = "share_ratio", nullable = false, precision = 5, scale = 4)
    private BigDecimal shareRatio = BigDecimal.ZERO;

    @Column(name = "running_balance", nullable = false, precision = 14, scale = 2)
    private BigDecimal runningBalance = BigDecimal.ZERO;

    @Column(name = "is_admin", nullable = false)
    private boolean admin = false;

    public SharedWalletMember() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SharedWallet getWallet() {
        return wallet;
    }

    public void setWallet(SharedWallet wallet) {
        this.wallet = wallet;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public BigDecimal getShareRatio() {
        return shareRatio;
    }

    public void setShareRatio(BigDecimal shareRatio) {
        this.shareRatio = shareRatio;
    }

    public BigDecimal getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(BigDecimal runningBalance) {
        this.runningBalance = runningBalance;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
