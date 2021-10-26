package com.deloitte.components;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.commons.LabeledResource;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class WebDAMAsset extends WCMUsePojo {

	private static final String PN_PATH = "path";
	private static final String PN_DAM_PATH = "pathDAM";
	private static final String PN_ALT = "alt";
	private static final String PN_WIDTH = "width";
	private static final String PN_HEIGHT = "height";
	private static final String PN_IS_DECORATIVE = "isDecorative";
	private static final String PN_LINK_URL = "linkURL";
	public static final String PN_FILE_TYPE = "filetype";
	public static final String PN_TYPE = "type";
	private static final String PN_DESCRIPTION = "description";

	private static final String PN_ID = "id";
	private static final String PN_RENDITION = "rendition";
	private static final String PN_VIDEO_URLS = "videoUrls";
	private static final String DEFAULT_RENDITION = "1280";
	public static final String VIDEO = "video";
	public static final String IMAGE = "image";
	public static final String STRING_ZERO = "0";


	Resource webDAMResource;
	private String url;
	private String name;
	private String title;
	private boolean configured = false;
	private String description;
	private boolean isVideo;
	private boolean isImage;
	private String rendition;
	private String cssClass;
	private String[] videoUrls;
	private String filetype;
	private String linkUrl;
	private String alt;
	private String width;
	private String height;
	private String id;
	private String parentId;
	public static final String[] VIDEO_FILE_TYPES = new String[] { "mp4" };
	public static final String[] IMAGE_FILE_TYPES = new String[] { "jpg", "png", "gif", "tiff", "jpeg" };

	@Override
	public void activate() throws Exception {
		String path = StringUtils.defaultIfBlank(this.get(PN_PATH, String.class),
				this.getProperties().get(PN_PATH, String.class));
		String damPath = StringUtils.defaultIfBlank(this.get(PN_DAM_PATH, String.class),
				this.getProperties().get(PN_DAM_PATH.concat("_"+DEFAULT_RENDITION), String.class));
		webDAMResource = this.getResourceResolver().getResource(path + "/jcr:content");

		if (damPath != null) {
			filetype = FilenameUtils.getExtension(damPath);
			if(isVideoType(filetype)){
				this.isVideo = true;
				this.isImage = false;
                this.cssClass = VIDEO;
			} else {
				this.isImage = true;
				this.isVideo = false;
                this.cssClass = IMAGE;
			}

			if (this.isVideo) {
				videoUrls = new String[]{damPath};
			} else if (this.isImage) {
				this.url = damPath;
				this.title = StringUtils.defaultIfBlank(this.get(JcrConstants.JCR_TITLE, String.class),
						this.getProperties().get(JcrConstants.JCR_TITLE, String.class));
				this.description = StringUtils.defaultIfBlank(this.get(JcrConstants.JCR_DESCRIPTION, String.class),
						this.getProperties().get(JcrConstants.JCR_DESCRIPTION, String.class));
				this.linkUrl = StringUtils.defaultIfBlank(this.get(PN_LINK_URL, String.class),
						this.getProperties().get(PN_LINK_URL, String.class));
				this.height = StringUtils.defaultIfBlank(this.get(PN_HEIGHT, String.class),
						this.getProperties().get(PN_HEIGHT, String.class));
				this.width = StringUtils.defaultIfBlank(this.get(PN_WIDTH, String.class),
						this.getProperties().get(PN_WIDTH, String.class));

				String isDecorative = this.getProperties().get(PN_IS_DECORATIVE, String.class);
				if (StringUtils.isBlank(isDecorative)) {
					this.alt = this.getProperties().get(PN_ALT, String.class);
				}
			}
			this.configured = true;
		} else if(webDAMResource != null) {
			rendition = StringUtils.defaultIfBlank(this.get(PN_RENDITION, String.class), StringUtils
					.defaultIfBlank(this.getProperties().get(PN_RENDITION, String.class), DEFAULT_RENDITION));

			width = StringUtils.defaultIfBlank(this.getProperties().get(PN_WIDTH, String.class), rendition);
			if (STRING_ZERO.equals(width)) {
				width = rendition;
			}
			height = this.getProperties().get(PN_HEIGHT, String.class);
			if (STRING_ZERO.equals(height)) {
				height = StringUtils.EMPTY;
			}

			String isDecorative = this.getProperties().get(PN_IS_DECORATIVE, String.class);
			if (StringUtils.isBlank(isDecorative)) {
				this.alt = this.getProperties().get(PN_ALT, String.class);
			}

			this.linkUrl = this.getProperties().get(PN_LINK_URL, String.class);

			ValueMap valueMap = webDAMResource.getValueMap();
			filetype = valueMap.get(PN_FILE_TYPE, String.class);
			id = valueMap.get(PN_ID, String.class);
			// parent/asset/jcr:content=webDamResource
			Resource parent = webDAMResource.getParent().getParent();
			if (parent != null) {
				Resource parentWebDAMResource = parent.getChild(JcrConstants.JCR_CONTENT);
				if (parentWebDAMResource != null) {
					parentId = parentWebDAMResource.getValueMap().get(PN_ID, String.class);
				}
			}
			this.url = valueMap.get("thumbnail_" + rendition, String.class);
			if (url == null) {
				this.url = StringUtils.EMPTY;
			}
			this.name = this.getResource().getName();

			LabeledResource labeledWebDAMResource = webDAMResource.adaptTo(LabeledResource.class);
			if (labeledWebDAMResource != null) {
				this.title = labeledWebDAMResource.getTitle();
				this.description = labeledWebDAMResource.getDescription();
			}

			LabeledResource labeledResource = this.getResource().adaptTo(LabeledResource.class);
			if (labeledResource != null) {
				this.title = StringUtils.defaultIfBlank(labeledResource.getTitle(), this.title);
				this.description = StringUtils.defaultIfBlank(labeledResource.getDescription(), this.description);
			}

			if (StringUtils.isBlank(this.description)) {
				this.description = valueMap.get(PN_DESCRIPTION, String.class);
			}

			this.configured = true;
			this.isVideo = isVideoType(filetype);
			this.isImage = isImageType(filetype);

			if (this.isVideo) {
				this.cssClass = VIDEO;
			} else if (this.isImage) {
				this.cssClass = IMAGE;
			}
		}
	}

	public static boolean isVideoType(String filetype) {
		return ArrayUtils.contains(VIDEO_FILE_TYPES, filetype);
	}

	public static boolean isImageType(String filetype) {
		return ArrayUtils.contains(IMAGE_FILE_TYPES, filetype);
	}

	public static boolean isFolderType(String type) {
		return "folder".equals(type);
	}

	public String getId() {
		return id;
	}

	public String getParentId() {
		return this.parentId;
	}

	public String getHeight() {
		return height;
	}

	public String getAlt() {
		return alt;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public Object getFiletype() {
		return filetype;
	}

	public String[] getVideoUrls() {
		return videoUrls;
	}

	public String getCssClass() {
		return cssClass;
	}

	public String getWidth() {
		return width;
	}

	public boolean isImage() {
		return isImage;
	}

	public boolean isVideo() {
		return isVideo;
	}

	public boolean isConfigured() {
		return configured;
	}

	public String getName() {
		return this.name;
	}

	public String getTitle() {
		return this.title;
	}

	public String getUrl() {
		return this.url;
	}

	public String getDescription() {
		return description;
	}
}
