package io.vithor.yamvpframework.extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Created by Hazer on 2/22/16.
 */

inline fun <reified T : Fragment> FragmentManager.find(tag: String): T? = findFragmentByTag(tag) as? T
inline fun <reified T : Fragment> FragmentManager.find(id: Int): T? = findFragmentById(id) as? T