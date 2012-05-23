package com.happytap.acro;

import java.util.Date;

public class TwitterUser {

    private int id;
    private String name;
    private String screenName;
    private String location;
    private String description;
    private String profileImageUrl;
    private String url;
    private boolean isProtected;
    private int followersCount;

    private Date statusCreatedAt;
    private long statusId = -1;
    private String statusText = null;
    private String statusSource = null;
    private boolean statusTruncated = false;
    private long statusInReplyToStatusId = -1;
    private int statusInReplyToUserId = -1;
    private boolean statusFavorited = false;
    private String statusInReplyToScreenName = null;

    private String profileBackgroundColor;
    private String profileTextColor;
    private String profileLinkColor;
    private String profileSidebarFillColor;
    private String profileSidebarBorderColor;
    private int friendsCount;
    private Date createdAt;
    private int favouritesCount;
    private int utcOffset;
    private String timeZone;
    private String profileBackgroundImageUrl;
    private boolean profileBackgroundTiled;
    private int statusesCount;
    private boolean isGeoEnabled;
    private boolean isVerified;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isProtected() {
		return isProtected;
	}
	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}
	public int getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	public Date getStatusCreatedAt() {
		return statusCreatedAt;
	}
	public void setStatusCreatedAt(Date statusCreatedAt) {
		this.statusCreatedAt = statusCreatedAt;
	}
	public long getStatusId() {
		return statusId;
	}
	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getStatusSource() {
		return statusSource;
	}
	public void setStatusSource(String statusSource) {
		this.statusSource = statusSource;
	}
	public boolean isStatusTruncated() {
		return statusTruncated;
	}
	public void setStatusTruncated(boolean statusTruncated) {
		this.statusTruncated = statusTruncated;
	}
	public long getStatusInReplyToStatusId() {
		return statusInReplyToStatusId;
	}
	public void setStatusInReplyToStatusId(long statusInReplyToStatusId) {
		this.statusInReplyToStatusId = statusInReplyToStatusId;
	}
	public int getStatusInReplyToUserId() {
		return statusInReplyToUserId;
	}
	public void setStatusInReplyToUserId(int statusInReplyToUserId) {
		this.statusInReplyToUserId = statusInReplyToUserId;
	}
	public boolean isStatusFavorited() {
		return statusFavorited;
	}
	public void setStatusFavorited(boolean statusFavorited) {
		this.statusFavorited = statusFavorited;
	}
	public String getStatusInReplyToScreenName() {
		return statusInReplyToScreenName;
	}
	public void setStatusInReplyToScreenName(String statusInReplyToScreenName) {
		this.statusInReplyToScreenName = statusInReplyToScreenName;
	}
	public String getProfileBackgroundColor() {
		return profileBackgroundColor;
	}
	public void setProfileBackgroundColor(String profileBackgroundColor) {
		this.profileBackgroundColor = profileBackgroundColor;
	}
	public String getProfileTextColor() {
		return profileTextColor;
	}
	public void setProfileTextColor(String profileTextColor) {
		this.profileTextColor = profileTextColor;
	}
	public String getProfileLinkColor() {
		return profileLinkColor;
	}
	public void setProfileLinkColor(String profileLinkColor) {
		this.profileLinkColor = profileLinkColor;
	}
	public String getProfileSidebarFillColor() {
		return profileSidebarFillColor;
	}
	public void setProfileSidebarFillColor(String profileSidebarFillColor) {
		this.profileSidebarFillColor = profileSidebarFillColor;
	}
	public String getProfileSidebarBorderColor() {
		return profileSidebarBorderColor;
	}
	public void setProfileSidebarBorderColor(String profileSidebarBorderColor) {
		this.profileSidebarBorderColor = profileSidebarBorderColor;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public int getFavouritesCount() {
		return favouritesCount;
	}
	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}
	public int getUtcOffset() {
		return utcOffset;
	}
	public void setUtcOffset(int utcOffset) {
		this.utcOffset = utcOffset;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}
	public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
		this.profileBackgroundImageUrl = profileBackgroundImageUrl;
	}
	public boolean isProfileBackgroundTiled() {
		return profileBackgroundTiled;
	}
	public void setProfileBackgroundTiled(boolean profileBackgroundTiled) {
		this.profileBackgroundTiled = profileBackgroundTiled;
	}
	public int getStatusesCount() {
		return statusesCount;
	}
	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}
	public boolean isGeoEnabled() {
		return isGeoEnabled;
	}
	public void setGeoEnabled(boolean isGeoEnabled) {
		this.isGeoEnabled = isGeoEnabled;
	}
	public boolean isVerified() {
		return isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	
}
