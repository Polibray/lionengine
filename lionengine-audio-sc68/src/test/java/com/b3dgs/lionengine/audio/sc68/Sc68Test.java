/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.audio.sc68;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.audio.Audio;
import com.b3dgs.lionengine.audio.AudioFactory;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.util.UtilEnum;
import com.b3dgs.lionengine.util.UtilFile;
import com.b3dgs.lionengine.util.UtilReflection;
import com.b3dgs.lionengine.util.UtilStream;
import com.b3dgs.lionengine.util.UtilTests;

/**
 * Test the sc68 player.
 */
public class Sc68Test
{
    /** Binding. */
    private Sc68 sc68;
    /** Media music. */
    private Media music;

    /**
     * Prepare tests.
     */
    @Before
    public void prepare()
    {
        try
        {
            AudioFactory.addFormat(new Sc68Format());
            Medias.setLoadFromJar(Sc68Test.class);
            music = Medias.create("music.sc68");
            sc68 = AudioFactory.loadAudio(music, Sc68.class);
        }
        catch (final LionEngineException exception)
        {
            final String message = exception.getMessage();
            Assert.assertTrue(message, message.contains(Sc68Format.ERROR_LOAD_LIBRARY));
            Assume.assumeFalse("Sc68 not supported on test machine - Test skipped",
                               message.contains(Sc68Format.ERROR_LOAD_LIBRARY));
        }
    }

    /**
     * Clean up tests.
     */
    @After
    public void cleanUp()
    {
        Medias.setLoadFromJar(null);
        AudioFactory.clearFormats();
    }

    /**
     * Test with <code>null</code> argument.
     */
    @Test(expected = LionEngineException.class)
    public void testNullArgument()
    {
        Assert.assertNotNull(AudioFactory.loadAudio(null, Sc68.class));
    }

    /**
     * Test with missing library.
     * 
     * @throws Exception If error.
     */
    @Test(expected = LionEngineException.class)
    public void testMissingLibrary() throws Exception
    {
        final Field field = Sc68Format.class.getDeclaredField("LIBRARY_NAME");
        final String back = UtilReflection.getField(Sc68Format.class, "LIBRARY_NAME");
        try
        {
            UtilEnum.setStaticFinal(field, "void");
            Assert.assertNull(new Sc68Format());
        }
        finally
        {
            UtilEnum.setStaticFinal(field, back);
        }
    }

    /**
     * Test with negative volume.
     */
    @Test(expected = LionEngineException.class)
    public void testNegativeVolume()
    {
        sc68.setVolume(-1);
        Assert.fail();
    }

    /**
     * Test with out of range volume.
     */
    @Test(expected = LionEngineException.class)
    public void testOutOfRangeVolume()
    {
        sc68.setVolume(101);
        Assert.fail();
    }

    /**
     * Test Sc68 sequence.
     * 
     * @throws InterruptedException If error.
     */
    @Test
    public void testSc68() throws InterruptedException
    {
        sc68.setStart(0L);
        sc68.setLoop(0L, 0L);

        sc68.setVolume(15);
        sc68.setConfig(true, false);
        sc68.play();
        Thread.sleep(500);
        sc68.setConfig(false, false);
        sc68.pause();
        Thread.sleep(500);
        sc68.setConfig(false, true);
        sc68.setVolume(30);
        sc68.resume();
        Thread.sleep(500);
        sc68.setConfig(true, true);
        Assert.assertTrue(sc68.getTicks() >= 0);
        sc68.stop();
    }

    /**
     * Test Sc68 stress.
     * 
     * @throws InterruptedException If error.
     */
    @Test
    public void testStress() throws InterruptedException
    {
        sc68.play();
        sc68.stop();
        sc68.play();
        Thread.sleep(Constant.HUNDRED);
        sc68.stop();
        sc68.play();
        sc68.pause();
        sc68.resume();
        for (int i = 0; i < 5; i++)
        {
            sc68.play();
            Thread.sleep(Constant.HUNDRED);
        }
        Thread.sleep(250);
        sc68.stop();
        sc68.play();
        sc68.stop();
    }

    /**
     * Test Sc68 with outside media.
     * 
     * @throws IOException If error.
     */
    @Test
    public void testOutsideMedia() throws IOException
    {
        InputStream input = null;
        OutputStream output = null;
        try
        {
            input = music.getInputStream();

            Medias.setLoadFromJar(null);
            Medias.setResourcesDirectory(System.getProperty("java.io.tmpdir"));

            final Media media = Medias.create("music.sc68");
            output = media.getOutputStream();
            UtilStream.copy(input, output);

            final Audio sc68 = AudioFactory.loadAudio(media);
            sc68.setVolume(50);
            sc68.play();
            UtilTests.pause(100L);
            sc68.stop();

            UtilStream.safeClose(output);
            UtilFile.deleteFile(media.getFile());
        }
        finally
        {
            Medias.setResourcesDirectory(Constant.EMPTY_STRING);
            UtilStream.safeClose(input);
            UtilStream.safeClose(output);
        }
    }
}
