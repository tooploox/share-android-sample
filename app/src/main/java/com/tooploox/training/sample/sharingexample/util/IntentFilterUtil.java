/*
Copyright 2015 Damian Walczak

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.tooploox.training.sample.sharingexample.util;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Damian Walczak
 * <p/>
 * Intent filtering util, that checks available apps for given messageIntent and sets the targets
 * to packages we'd like to have with higher priority.
 * If the packages are not matched, it should fallback to standard picker.
 */
public class IntentFilterUtil {

    private static final String[] EMAIL_PACKAGES = new String[]{
            "android.gm",                   // Gmail
            "android.email",                // Android mail
            "kman.AquaMail",                // Aqua Mail
            "trtf.blue",                    // Blue Mail
            "cloudmagic.mail",              // CloudMagic
            "syntomo.email",                // Email for Exchange
            "android.apps.inbox",           // Inbox by Gmail
            "fsck.k9",                      // K-9 mail
            "mail.mailapp",                 // Mail.ru
            "my.mail",                      // myMail
            "com.wemail",                   // WeMail
            "mobile.client.android.mail"    // Yahoo mail
    };

    private static final String[] MSG_PACKAGES = new String[]{
            "mms",                          // Android SMS app
            "p1.chompsms",                  // chomp SMS
            "nextsms",                      // Handcent SMS
            "jb.gosms",                     // Go SMS
            "android.talk",                 // Hangouts
            "hellotext.hello",              // Hello SMS
            "ninja.sms.promo",              // Ninja SMS (HoverChat)
            "com.textra",                   // Textra SMS
    };

    private IntentFilterUtil() {
    }

    public static boolean isPackageInstalled(PackageManager pm, Intent messageIntent, String... lookupPackages) {
        return !findMatchingResolveInfo(pm, messageIntent, lookupPackages).isEmpty();
    }

    public static Intent filterSendAction(PackageManager pm, Intent messageIntent, String... lookupPackages) {
        Collection<ResolveInfo> matchingResInfo = findMatchingResolveInfo(pm, messageIntent, lookupPackages);

        if (!matchingResInfo.isEmpty()) {
            List<LabeledIntent> intentList = new ArrayList<>();
            if (matchingResInfo.size() == 1) {
                messageIntent.setPackage(matchingResInfo.iterator().next().activityInfo.packageName);
            } else {
                for (ResolveInfo resolveInfo : matchingResInfo) {
                    Intent intent = new Intent(messageIntent);
                    ActivityInfo activityInfo = resolveInfo.activityInfo;
                    intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                    intentList.add(new LabeledIntent(intent, activityInfo.packageName, resolveInfo.loadLabel(pm), resolveInfo.icon));
                }
                LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
                messageIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            }
        }
        return messageIntent;
    }

    public static Intent filterEmailAction(PackageManager pm, Intent intent) {
        return IntentFilterUtil.filterSendAction(pm, intent, EMAIL_PACKAGES);
    }

    public static Intent filterMessageAction(PackageManager pm, Intent intent) {
        return IntentFilterUtil.filterSendAction(pm, intent, MSG_PACKAGES);
    }

    public static Collection<ResolveInfo> findMatchingEmail(PackageManager pm, Intent messageIntent) {
        return findMatchingResolveInfo(pm, messageIntent, EMAIL_PACKAGES);
    }

    public static Collection<ResolveInfo> findMatchingMessage(PackageManager pm, Intent messageIntent) {
        return findMatchingResolveInfo(pm, messageIntent, MSG_PACKAGES);
    }

    public static Collection<ResolveInfo> findAllMatching(PackageManager pm, Intent messageIntent) {
        return pm.queryIntentActivities(messageIntent, 0);
    }

    static Collection<ResolveInfo> findMatchingResolveInfo(PackageManager pm, Intent messageIntent, String... lookupPackages) {
        Collection<ResolveInfo> resInfo = findAllMatching(pm, messageIntent);
        Collection<ResolveInfo> matchingResInfo = new HashSet<>(resInfo.size());
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            for (String lookupPackage : lookupPackages) {
                if (packageName.contains(lookupPackage)) {
                    matchingResInfo.add(resolveInfo);
                    break;
                }
            }
        }
        return matchingResInfo;
    }
}
