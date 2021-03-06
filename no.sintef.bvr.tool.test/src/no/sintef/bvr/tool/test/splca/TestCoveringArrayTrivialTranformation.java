package no.sintef.bvr.tool.test.splca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import no.sintef.bvr.common.logging.ConsoleLogger;
import no.sintef.bvr.test.common.utils.TestProject;
import no.sintef.bvr.test.common.utils.TestResourceHolder;
import no.sintef.bvr.test.common.utils.TestUtils;
import no.sintef.bvr.tool.context.Context;
import no.sintef.bvr.tool.logging.impl.DefaultLogger;
import no.sintef.bvr.tool.model.BVRSimpleToolModel;
import no.sintef.bvr.tool.model.BVRToolModel;
import no.sintef.bvr.tool.test.Activator;
import no.sintef.ict.splcatool.BVRException;
import no.sintef.ict.splcatool.CSVException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bvr.BVRModel;
import bvr.CompoundResolution;
import bvr.NegResolution;
import bvr.VSpecResolution;

public class TestCoveringArrayTrivialTranformation {

	private static String[] testFolders = { "TestFolder" };

	private static TestResourceHolder[] resources = { new TestResourceHolder("/resources/vm/gen_product_src.bvr", "TestFolder/gen_product_src.bvr"),
			new TestResourceHolder("/resources/vm/gen_product_trg.bvr", "TestFolder/gen_product_trg.bvr"),
			new TestResourceHolder("/resources/vm/trivial_validation_not_null.bvr", "TestFolder/trivial_validation_not_null.bvr") };

	/** The test project. */
	private static TestProject testProject;

	@BeforeClass
	public static void setUpClass() throws CoreException, IOException {
		Context.eINSTANCE.setIWorkbenchWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		Context.eINSTANCE.logger = new ConsoleLogger();
		Context.eINSTANCE.problemLogger = new DefaultLogger();
		testProject = new TestProject("AddVSpecTargetTest", Activator.PLUGIN_ID);
		testProject.closeWelcome();
		testProject.createFolders(testFolders);
		testProject.createResources(resources);
	}

	@AfterClass
	public static void tearDownClass() throws CoreException {
		testProject.disposeTestProject();
	}

	private BVRToolModel transactionModel;

	private BVRModel bvrModel;

	private BVRToolModel simpleToolModelTarget;

	private BVRModel bvrModelTarget;

	private BVRToolModel transactionModelResolution;

	@Before
	public void setUp() throws Exception {
		transactionModel = Context.eINSTANCE.testBVRToolModel(resources[0].getiFile().getLocation().toFile());
		assertNotNull(transactionModel);

		transactionModelResolution = Context.eINSTANCE.testBVRToolModel(resources[2].getiFile().getLocation().toFile());
		assertNotNull(transactionModelResolution);

		bvrModel = transactionModel.getBVRModel();
		assertNotNull(bvrModel);

		simpleToolModelTarget = createBVRToolModel(resources[1].getiFile().getLocation().toFile().getAbsolutePath());
		assertNotNull(simpleToolModelTarget);

		bvrModelTarget = simpleToolModelTarget.getBVRModel();
		assertNotNull(bvrModelTarget);
	}

	@After
	public void tearDown() throws Exception {
		Context.eINSTANCE.getBvrModels().clear();
		Context.eINSTANCE.getBvrViews().clear();
		Context.eINSTANCE.disposeModel(transactionModel);
		Context.eINSTANCE.disposeModel(transactionModelResolution);
		TransactionalEditingDomain.Registry.INSTANCE.remove("no.sintef.bvr.shared.EditingDomain");
		Context.eINSTANCE.getEditorCommands().disableBatchProcessing();
	}

	@Test
	public void testSingleResolutionValidationNotNull() {
		BVRModel bvr_model = transactionModelResolution.getBVRModel();
		assertTrue(bvr_model.getResolutionModels().size() == 1);

		CompoundResolution resolution = bvr_model.getResolutionModels().get(0);

		try {
			transactionModelResolution.performSATValidation(resolution);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Validation failed with exception: " + e.getMessage(), true);
		}
	}

	@Test
	public void testAllResolutionsValidationNotNull() {
		BVRModel bvr_model = transactionModelResolution.getBVRModel();
		assertTrue(bvr_model.getResolutionModels().size() != 0);

		try {
			transactionModelResolution.performSATValidation();
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Validation failed with exception: " + e.getMessage(), true);
		}
	}

	@Test
	public void testTrivialProductGeneration() throws BVRException, CSVException {
		transactionModel.generateCoveringArray(2);

		EList<CompoundResolution> resolutions = bvrModel.getResolutionModels();
		assertEquals("Wrong number of resolution is generated", bvrModelTarget.getResolutionModels().size(), resolutions.size());

		List<Map<String, Boolean>> producedProducts = transactionModel.getSPLCABVRModel().extractResolvedVSpecProducts();
		List<Map<String, Boolean>> expectedProducts = simpleToolModelTarget.getSPLCABVRModel().extractResolvedVSpecProducts();

		assertTrue("Actual product list is incorrect: expected -->" + expectedProducts + " actual -->" + producedProducts,
				TestUtils.compareProductResalutionsEqual(expectedProducts, producedProducts));

	}

	@Test
	public void testTrivialAllProductGeneration() throws BVRException, CSVException {
		transactionModel.generatAllProducts();

		EList<CompoundResolution> resolutions = bvrModel.getResolutionModels();
		assertEquals("Wrong number of resolution is generated", bvrModelTarget.getResolutionModels().size(), resolutions.size());

		List<Map<String, Boolean>> producedProducts = transactionModel.getSPLCABVRModel().extractResolvedVSpecProducts();
		List<Map<String, Boolean>> expectedProducts = simpleToolModelTarget.getSPLCABVRModel().extractResolvedVSpecProducts();

		assertTrue("Actual product list is incorrect: expected -->" + expectedProducts + " actual -->" + producedProducts,
				TestUtils.compareProductResalutionsEqual(expectedProducts, producedProducts));

	}

	@Test
	public void testTrivialAllProductValidation() throws BVRException, CSVException {
		transactionModel.generatAllProducts();

		EList<CompoundResolution> resolutions = bvrModel.getResolutionModels();
		assertEquals("Wrong number of resolution is generated", bvrModelTarget.getResolutionModels().size(), resolutions.size());

		boolean result = transactionModel.performSATValidation();
		assertTrue("product is actuall valid, however false is reported", result);
	}

	@Test
	public void testTrivialSingleProductValidation() throws BVRException, CSVException {
		transactionModel.generatAllProducts();

		EList<CompoundResolution> resolutions = bvrModel.getResolutionModels();
		assertEquals("Wrong number of resolution is generated", bvrModelTarget.getResolutionModels().size(), resolutions.size());

		boolean result = transactionModel.performSATValidation(resolutions.get(0));
		assertTrue("product is actuall valid, however invalid is reported", result);
	}

	@Test
	public void testTrivialSingleChoiceValidation() throws BVRException, CSVException {
		transactionModel.generatAllProducts();

		EList<CompoundResolution> resolutions = bvrModel.getResolutionModels();
		assertEquals("Wrong number of resolution is generated", bvrModelTarget.getResolutionModels().size(), resolutions.size());

		VSpecResolution resolution = findNegativelyResolvedChoice(resolutions);
		assertNotNull(resolution);

		boolean result = transactionModel.performSATValidation(resolution);
		assertFalse("NegResolution is actuall invalid, however valid is reported", result);
	}

	@Test
	public void testTrivialSingleChoiceValidation1() throws BVRException, CSVException {
		transactionModel.generatAllProducts();

		EList<CompoundResolution> resolutions = bvrModel.getResolutionModels();
		assertEquals("Wrong number of resolution is generated", bvrModelTarget.getResolutionModels().size(), resolutions.size());

		VSpecResolution resolution = findNegativelyResolvedChoice(resolutions);
		assertNotNull(resolution);

		List<String> result = transactionModel.validateChoiceResolution(resolution);
		assertFalse("NegResolution is actuall invalid, however valid is reported", result.size() == 0);
	}

	private VSpecResolution findNegativelyResolvedChoice(EList<CompoundResolution> resolutions) {
		for (CompoundResolution resolution : resolutions) {
			EList<VSpecResolution> res = resolution.getMembers();
			for (VSpecResolution vspecres : res) {
				if (vspecres instanceof NegResolution)
					return vspecres;
			}
		}
		return null;
	}

	private BVRToolModel createBVRToolModel(String filename) {
		BVRToolModel model = new BVRSimpleToolModel(new File(filename));
		return model;
	}
}
