package com.example.e_commerce1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity representing customers and admins in the e-commerce system.
 * 
 * In a real application, this would integrate with:
 * - Spring Security for authentication
 * - OAuth2 for social login
 * - Email verification service
 * - Password encryption (BCrypt)
 * 
 * Security Note: Passwords should NEVER be stored in plain text!
 * Always use BCryptPasswordEncoder or similar.
 * 
 * @author Your Name
 */
@Document(collection = "users")
public class User {

    @Id
    private String id;
    
    // Authentication credentials
    private String username;
    private String email;
    private String password;  // Should be encrypted with BCrypt in real app
    private String phoneNumber;
    
    // User profile
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private Instant dateOfBirth;
    
    // Account settings
    private String role;  // "CUSTOMER", "ADMIN", "VENDOR"
    private boolean isActive;  // Account status
    private boolean emailVerified;
    private boolean phoneVerified;
    
    // Addresses - users can have multiple addresses
    private Address defaultShippingAddress;
    private Address defaultBillingAddress;
    private List<Address> savedAddresses;
    
    // Account activity tracking
    private Instant accountCreatedAt;
    private Instant lastLoginAt;
    private Instant lastPasswordChangeAt;
    
    // Preferences for personalization
    private List<String> favoriteCategories;
    private boolean subscribeToNewsletter;
    private String preferredLanguage;  // "en", "hi", etc.
    private String preferredCurrency;  // "INR", "USD", etc.
    
    // Loyalty program (for future features)
    private int loyaltyPoints;
    private String membershipTier;  // "BRONZE", "SILVER", "GOLD"

    /**
     * Default constructor with sensible defaults.
     */
    public User() {
        this.savedAddresses = new ArrayList<>();
        this.favoriteCategories = new ArrayList<>();
        this.accountCreatedAt = Instant.now();
        this.isActive = true;
        this.emailVerified = false;
        this.phoneVerified = false;
        this.role = "CUSTOMER";  // Default role
        this.subscribeToNewsletter = false;
        this.loyaltyPoints = 0;
        this.membershipTier = "BRONZE";
        this.preferredLanguage = "en";
        this.preferredCurrency = "INR";
    }

    /**
     * Helper method to get full name.
     * Useful for displaying user info in UI.
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return username;  // Fallback to username
    }

    /**
     * Checks if user is an admin.
     * Used for authorization checks.
     */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    /**
     * Updates last login timestamp.
     * Called after successful authentication.
     */
    public void recordLogin() {
        this.lastLoginAt = Instant.now();
    }

    /**
     * Adds loyalty points when user makes a purchase.
     * Example: 1 point per 100 INR spent.
     */
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
        updateMembershipTier();
    }

    /**
     * Automatically upgrades membership tier based on points.
     * This encourages repeat purchases!
     */
    private void updateMembershipTier() {
        if (loyaltyPoints >= 10000) {
            this.membershipTier = "GOLD";
        } else if (loyaltyPoints >= 5000) {
            this.membershipTier = "SILVER";
        } else {
            this.membershipTier = "BRONZE";
        }
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.lastPasswordChangeAt = Instant.now();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Instant getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Address getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(Address defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public Address getDefaultBillingAddress() {
        return defaultBillingAddress;
    }

    public void setDefaultBillingAddress(Address defaultBillingAddress) {
        this.defaultBillingAddress = defaultBillingAddress;
    }

    public List<Address> getSavedAddresses() {
        return savedAddresses;
    }

    public void setSavedAddresses(List<Address> savedAddresses) {
        this.savedAddresses = savedAddresses;
    }

    public Instant getAccountCreatedAt() {
        return accountCreatedAt;
    }

    public void setAccountCreatedAt(Instant accountCreatedAt) {
        this.accountCreatedAt = accountCreatedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Instant getLastPasswordChangeAt() {
        return lastPasswordChangeAt;
    }

    public void setLastPasswordChangeAt(Instant lastPasswordChangeAt) {
        this.lastPasswordChangeAt = lastPasswordChangeAt;
    }

    public List<String> getFavoriteCategories() {
        return favoriteCategories;
    }

    public void setFavoriteCategories(List<String> favoriteCategories) {
        this.favoriteCategories = favoriteCategories;
    }

    public boolean isSubscribeToNewsletter() {
        return subscribeToNewsletter;
    }

    public void setSubscribeToNewsletter(boolean subscribeToNewsletter) {
        this.subscribeToNewsletter = subscribeToNewsletter;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getMembershipTier() {
        return membershipTier;
    }

    public void setMembershipTier(String membershipTier) {
        this.membershipTier = membershipTier;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", membershipTier='" + membershipTier + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    /**
     * Nested class representing an address.
     * Users can have multiple addresses saved.
     */
    public static class Address {
        private String label;  // "Home", "Office", "Parent's House"
        private String fullName;  // Recipient name
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String phoneNumber;
        private boolean isDefault;

        public Address() {}

        // Getters and Setters

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean aDefault) {
            isDefault = aDefault;
        }

        /**
         * Formats address for display on labels or UI.
         */
        public String getFormattedAddress() {
            StringBuilder sb = new StringBuilder();
            if (fullName != null) sb.append(fullName).append("\n");
            if (addressLine1 != null) sb.append(addressLine1).append("\n");
            if (addressLine2 != null && !addressLine2.isEmpty()) sb.append(addressLine2).append("\n");
            if (city != null) sb.append(city).append(", ");
            if (state != null) sb.append(state).append(" ");
            if (postalCode != null) sb.append(postalCode).append("\n");
            if (country != null) sb.append(country);
            return sb.toString();
        }
    }
}



