/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bplaced.clayn.cfs.test.util;

import net.bplaced.clayn.cfs.test.BaseCFSTest;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public abstract class FileSystemMirrorTest extends BaseCFSTest
{

    protected static final String TEST_CREATE = "newFile";
    protected static final String TEST_MODIFY = "modify";
    protected static final String TEST_DELETE = "delete";

    protected static final String[] TEST_ALL = new String[]
    {
        TEST_CREATE,
        TEST_DELETE,
        TEST_MODIFY
    };
}
