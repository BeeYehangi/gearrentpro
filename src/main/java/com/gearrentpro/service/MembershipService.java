package com.gearrentpro.service;

import com.gearrentpro.dao.MembershipDAO;
import com.gearrentpro.entity.Membership;

import java.sql.SQLException;
import java.util.List;

public class MembershipService {

    private MembershipDAO membershipDAO = new MembershipDAO();

    public List<Membership> getAllMemberships() throws SQLException {
        return membershipDAO.findAll();
    }

    public Membership getMembershipById(String id) throws SQLException {
        return membershipDAO.findById(id);
    }

    public void addMembership(Membership membership) throws SQLException {
        if (membership.getMembershipId() == null || 
            membership.getMembershipId().isEmpty()) {
            throw new IllegalArgumentException("Membership ID is required.");
        }
        if (membership.getLevelName() == null || 
            membership.getLevelName().isEmpty()) {
            throw new IllegalArgumentException("Level name is required.");
        }
        membershipDAO.save(membership);
    }

    public void updateMembership(Membership membership) throws SQLException {
        if (membership.getLevelName() == null || 
            membership.getLevelName().isEmpty()) {
            throw new IllegalArgumentException("Level name is required.");
        }
        membershipDAO.update(membership);
    }

    public void deleteMembership(String membershipId) throws SQLException {
        membershipDAO.delete(membershipId);
    }
}