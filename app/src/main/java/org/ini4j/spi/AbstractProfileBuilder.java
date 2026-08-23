/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import org.ini4j.CommentedMap;
import org.ini4j.Config;
import org.ini4j.Profile;
import org.ini4j.spi.IniHandler;

abstract class AbstractProfileBuilder
implements IniHandler {
    private Profile.Section _currentSection;
    private boolean _header;
    private String _lastComment;

    AbstractProfileBuilder() {
    }

    private void putComment(CommentedMap<String, ?> commentedMap, String string2) {
        if (this.getConfig().isComment()) {
            commentedMap.putComment(string2, this._lastComment);
        }
    }

    private void setHeaderComment() {
        if (this.getConfig().isComment()) {
            this.getProfile().setComment(this._lastComment);
        }
    }

    @Override
    public void endIni() {
        if (this._lastComment != null && this._header) {
            this.setHeaderComment();
        }
    }

    @Override
    public void endSection() {
        this._currentSection = null;
    }

    abstract Config getConfig();

    Profile.Section getCurrentSection() {
        return this._currentSection;
    }

    abstract Profile getProfile();

    @Override
    public void handleComment(String string2) {
        if (this._lastComment != null && this._header) {
            this._header = false;
            this.setHeaderComment();
        }
        this._lastComment = string2;
    }

    @Override
    public void handleOption(String string2, String string3) {
        this._header = false;
        if (this.getConfig().isMultiOption()) {
            this._currentSection.add(string2, string3);
        } else {
            this._currentSection.put(string2, string3);
        }
        if (this._lastComment != null) {
            this.putComment(this._currentSection, string2);
            this._lastComment = null;
        }
    }

    @Override
    public void startIni() {
        if (this.getConfig().isHeaderComment()) {
            this._header = true;
        }
    }

    @Override
    public void startSection(String string2) {
        if (this.getConfig().isMultiSection()) {
            this._currentSection = this.getProfile().add(string2);
        } else {
            Profile.Section section = (Profile.Section)this.getProfile().get(string2);
            if (section == null) {
                section = this.getProfile().add(string2);
            }
            this._currentSection = section;
        }
        if (this._lastComment != null) {
            if (this._header) {
                this.setHeaderComment();
            } else {
                this.putComment(this.getProfile(), string2);
            }
            this._lastComment = null;
        }
        this._header = false;
    }
}

