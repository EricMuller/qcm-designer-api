package com.emu.apps.qcm.services.jpa;

import com.emu.apps.qcm.services.CategoryService;
import com.emu.apps.qcm.services.entity.category.Category;
import com.emu.apps.qcm.services.entity.category.Type;
import com.emu.apps.qcm.services.exceptions.FunctionnalException;
import com.emu.apps.qcm.services.jpa.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static com.emu.apps.qcm.services.entity.category.Category.ROOT_NAME;
import static com.emu.apps.qcm.services.jpa.repositories.mptt.MpttExceptions.*;

/**
 * Created by eric on 14/06/2017.
 */
@Service
@Transactional()
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Iterable <Category> findCategories(String userId, Type type) throws FunctionnalException {
        Category category = findOrCreateRoot(userId, type);
        return categoryRepository.findSubTree(category);
    }

    @Override
    public Iterable <Category> findChildrenCategories(long parentId) {
        return categoryRepository.findSubTree(categoryRepository.findById(parentId));
    }

    @Override
    public Category saveCategory(Category category) throws FunctionnalException {
        return findOrCreateByLibelle(category.getUserId(), category.getType(), category.getLibelle());
    }

    @Override
    public Optional <Category> findById(Long id) {
        return Optional.of(categoryRepository.findById(id));
    }

    public Optional <Category> findRootByUserId(String userId) {
        try {
            return Optional.of(categoryRepository.findHierarchyRoot(userId));
        } catch (HierarchyRootDoesNotExistException e) {
            return Optional.empty();
        }
    }

    public Optional <Category> findRootByUserIdAndType(String userId, Type type)  {

        Optional <Category> userRoot = findRootByUserId(userId);

        if (userRoot.isPresent()) {

            Category userCategory = userRoot.get();

            return categoryRepository.findSubTree(userCategory)
                    .stream()
                    .filter(c -> type.equals(c.getType()))
                    .findFirst();

        }
        return Optional.empty();
    }

    public Category findOrCreateRoot(String userId) throws HierarchyIdAlreadySetException, HierarchyRootExistsException {
        Category category;
        try {
            category = categoryRepository.findHierarchyRoot(userId);
        } catch (HierarchyRootDoesNotExistException e) {
            category = new Category(ROOT_NAME);
            categoryRepository.setAsUserRoot(category, userId);
        }
        return category;
    }

    public Category findOrCreateRoot(String userId, Type type) throws FunctionnalException {
        try {

            Category userRoot = findOrCreateRoot(userId);

            Optional <Category> category = categoryRepository.findSubTree(userRoot)
                    .stream()
                    .filter(c -> type.equals(c.getType()))
                    .findFirst();

            return category.isPresent() ? category.get() :
                    categoryRepository.addChild(userRoot, new Category(type, type.name()));

        } catch (HierarchyIdAlreadySetException | HierarchyRootExistsException | HierarchyIdNotSetException e) {
            throw new FunctionnalException("Error create tree " + type.name(), e);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Category findOrCreateByLibelle(String userId, Type type, String libelle) throws FunctionnalException {

        try {
            Category categoryRoot = findOrCreateRoot(userId, type);

            return getCategory(categoryRoot, type, libelle);

        } catch (HierarchyIdAlreadySetException | HierarchyIdNotSetException e) {
            throw new FunctionnalException("Error create category ", e);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Category findOrCreateChildByLibelle(Long parentId, Type type, String libelle) throws FunctionnalException {

        try {
            Category parent = categoryRepository.findById(parentId);
            return getCategory(parent, type, libelle);
        } catch (HierarchyIdAlreadySetException | HierarchyIdNotSetException e) {
            throw new FunctionnalException("Error create category ", e);
        }

    }

    private Category getCategory(Category parent, Type type, String libelle) throws HierarchyIdAlreadySetException, HierarchyIdNotSetException {

        Set <Category> categories = categoryRepository.findSubTree(parent);

        Optional <Category> category = categories
                .stream()
                .filter(c -> libelle.equals(c.getLibelle()))
                .findFirst();

        return category.isPresent() ? category.get() : categoryRepository.addChild(parent, new Category(type, libelle));
    }

}
