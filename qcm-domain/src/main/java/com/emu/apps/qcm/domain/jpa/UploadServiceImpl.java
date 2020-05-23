/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c)  2019 qcm-rest-api
 * Author  Eric Muller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.emu.apps.qcm.domain.jpa;

import com.emu.apps.qcm.domain.UploadDOService;
import com.emu.apps.qcm.domain.entity.upload.Upload;
import com.emu.apps.qcm.domain.jpa.repositories.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by eric on 05/06/2017.
 */
@Service
@Transactional
public class UploadServiceImpl implements UploadDOService {

    private final UploadRepository uploadRepository;

    @Autowired
    public UploadServiceImpl(UploadRepository uploadRepository) {
        this.uploadRepository = uploadRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Upload> findById(Long id) {
        return uploadRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        uploadRepository.deleteById(id);
    }

    @Override
    public Upload saveUpload(Upload upload) {
        return uploadRepository.save(upload);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Upload> findAllByPage(Specification<Upload> specification, Pageable pageable) {
        return uploadRepository.findAll(specification, pageable);
    }

}
