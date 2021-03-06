package com.skyisland.questmaker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.skyisland.questmaker.actions.ExitAction;
import com.skyisland.questmaker.actions.OpenProjectAction;
import com.skyisland.questmaker.configutils.FakeLocationState;
import com.skyisland.questmaker.configutils.ShallowItemMeta;
import com.skyisland.questmaker.configutils.ShallowItemStack;
import com.skyisland.questmaker.configutils.ShallowPotionEffectType;
import com.skyisland.questmaker.editor.Editor;
import com.skyisland.questmaker.editor.spell.SpellWindow;
import com.skyisland.questmaker.explorer.ProjectExplorer;
import com.skyisland.questmaker.npc.BankNPC;
import com.skyisland.questmaker.npc.DummyNPC;
import com.skyisland.questmaker.npc.ForgeNPC;
import com.skyisland.questmaker.npc.InnNPC;
import com.skyisland.questmaker.npc.LevelupNPC;
import com.skyisland.questmaker.npc.MuteNPC;
import com.skyisland.questmaker.npc.ServiceNPC;
import com.skyisland.questmaker.npc.ShopNPC;
import com.skyisland.questmaker.npc.SimpleBioptionNPC;
import com.skyisland.questmaker.npc.SimpleChatNPC;
import com.skyisland.questmaker.npc.SimpleQuestStartNPC;
import com.skyisland.questmaker.npc.TeleportNPC;
import com.skyisland.questmaker.project.Project;
import com.skyisland.questmanager.QuestManagerPlugin;
import com.skyisland.questmanager.configuration.utils.Chest;
import com.skyisland.questmanager.enemy.DefaultEnemy;
import com.skyisland.questmanager.enemy.NormalEnemy;
import com.skyisland.questmanager.enemy.StandardEnemy;
import com.skyisland.questmanager.fanciful.FancyMessage;
import com.skyisland.questmanager.fanciful.MessagePart;
import com.skyisland.questmanager.fanciful.TextualComponent;
import com.skyisland.questmanager.loot.Loot;
import com.skyisland.questmanager.magic.ImbuementSet;
import com.skyisland.questmanager.magic.spell.ChargeSpell;
import com.skyisland.questmanager.magic.spell.SimpleSelfSpell;
import com.skyisland.questmanager.magic.spell.SimpleTargetSpell;
import com.skyisland.questmanager.magic.spell.SpellWeavingSpell;
import com.skyisland.questmanager.magic.spell.effect.AreaEffect;
import com.skyisland.questmanager.magic.spell.effect.BlockEffect;
import com.skyisland.questmanager.magic.spell.effect.CastPylonEffect;
import com.skyisland.questmanager.magic.spell.effect.DamageEffect;
import com.skyisland.questmanager.magic.spell.effect.DamageMPEffect;
import com.skyisland.questmanager.magic.spell.effect.DamageUndeadEffect;
import com.skyisland.questmanager.magic.spell.effect.FireEffect;
import com.skyisland.questmanager.magic.spell.effect.HealEffect;
import com.skyisland.questmanager.magic.spell.effect.InvokeSpellWeavingEffect;
import com.skyisland.questmanager.magic.spell.effect.StatusEffect;
import com.skyisland.questmanager.magic.spell.effect.SummonTamedEffect;
import com.skyisland.questmanager.magic.spell.effect.SwapEffect;
import com.skyisland.questmanager.npc.utils.BankStorageManager;
import com.skyisland.questmanager.npc.utils.ServiceCraft;
import com.skyisland.questmanager.npc.utils.ServiceOffer;
import com.skyisland.questmanager.player.Party;
import com.skyisland.questmanager.player.PlayerOptions;
import com.skyisland.questmanager.player.QuestPlayer;
import com.skyisland.questmanager.region.CuboidRegion;
import com.skyisland.questmanager.region.SphericalRegion;
import com.skyisland.questmanager.ui.menu.inventory.ServiceInventory;
import com.skyisland.questmanager.ui.menu.inventory.ShopInventory;
import com.skyisland.questmanager.ui.menu.message.BioptionMessage;
import com.skyisland.questmanager.ui.menu.message.SimpleMessage;
import com.skyisland.questmanager.ui.menu.message.TreeMessage;

/**
 * Main driver class. Starts the program, pops up the window, and starts the ball rolling.
 * @author Skyler
 *
 */
public class Driver {
	
	public static final String MAIN_TITLE = "QuestMaker";
	
	private static final Dimension DEFAULT_SIZE = new Dimension(1200, 900);

	//Gui members
	
	public static Driver driver;
	
	public static Menus menus;
	
	private JFrame mainWindow;
	
	private JPanel fileViewer;
	
	private JDesktopPane editorPane;
	
	private JToolBar toolBar;
	
	
	
	
	//Project members
	
	private Project openProject;
	
	private ProjectExplorer explorer;
	
	private Editor editor;
	
	
	
	
	
	public static void main(String[] args) {
		QuestManagerPlugin.logger = Logger.getLogger("QuestMaker");
		//QuestManagerPlugin.registerConfigurationClasses();
		registerConfigurationClasses(); //do our own, a little custom
		//CraftItemFactory.instance();
		//QuestManagerPlugin.questManagerPlugin.
		
		
		driver = new Driver();
	}
	
	private static void registerConfigurationClasses() {
		if (QuestManagerPlugin.bukkit)
			return;
		
		ConfigurationSerialization.registerClass(ShallowItemStack.class, "org.bukkit.inventory.ItemStack");
		ConfigurationSerialization.registerClass(ShallowItemMeta.class, "ItemMeta");
		ConfigurationSerialization.registerClass(PotionEffect.class, "PotionEffect");
		
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("ABSORPTION", PotionEffectType.ABSORPTION));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("BLINDNESS", PotionEffectType.BLINDNESS));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("CONFUSION", PotionEffectType.CONFUSION));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("DAMAGE_RESISTANCE", PotionEffectType.DAMAGE_RESISTANCE));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("FAST_DIGGING", PotionEffectType.FAST_DIGGING));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("FIRE_RESISTANCE", PotionEffectType.FIRE_RESISTANCE));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("GLOWING", PotionEffectType.GLOWING));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("HARM", PotionEffectType.HARM));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("HEAL", PotionEffectType.HEAL));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("HEALTH_BOOST", PotionEffectType.HEALTH_BOOST));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("HUNGER", PotionEffectType.HUNGER));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("INCREASE_DAMAGE", PotionEffectType.INCREASE_DAMAGE));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("INVISIBILITY", PotionEffectType.INVISIBILITY));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("JUMP", PotionEffectType.JUMP));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("LEVITATION", PotionEffectType.LEVITATION));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("LUCK", PotionEffectType.LUCK));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("NIGHT_VISION", PotionEffectType.NIGHT_VISION));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("POISON", PotionEffectType.POISON));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("REGENERATION", PotionEffectType.REGENERATION));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("SATURATION", PotionEffectType.SATURATION));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("SLOW", PotionEffectType.SLOW));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("SLOW_DIGGING", PotionEffectType.SLOW_DIGGING));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("SPEED", PotionEffectType.SPEED));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("UNLUCK", PotionEffectType.UNLUCK));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("WATER_BREATHING", PotionEffectType.WATER_BREATHING));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("WEAKNESS", PotionEffectType.WEAKNESS));
		PotionEffectType.registerPotionEffectType(new ShallowPotionEffectType("WITHER", PotionEffectType.WITHER));
		PotionEffectType.stopAcceptingRegistrations();
		
		
		FakeLocationState.registerWithAliases();
		QuestPlayer.registerWithAliases();
		Party.registerWithAliases();
		MuteNPC.registerWithAliases();
		SimpleChatNPC.registerWithAliases();
		SimpleBioptionNPC.registerWithAliases();
		SimpleQuestStartNPC.registerWithAliases();
		InnNPC.registerWithAliases();
		ForgeNPC.registerWithAliases();
		ShopNPC.registerWithAliases();
		TeleportNPC.registerWithAliases();
		SimpleMessage.registerWithAliases();
		BioptionMessage.registerWithAliases();
		TreeMessage.registerWithAliases();
		ShopInventory.registerWithAliases();
		ServiceInventory.registerWithAliases();
		ServiceCraft.registerWithAliases();
		ServiceOffer.registerWithAliases();
		ServiceNPC.registerWithAliases();
		LevelupNPC.registerWithAliases();
		DummyNPC.registerWithAliases();
		ConfigurationSerialization.registerClass(MessagePart.class);
		ConfigurationSerialization.registerClass(TextualComponent.ArbitraryTextTypeComponent.class);
		ConfigurationSerialization.registerClass(TextualComponent.ComplexTextTypeComponent.class);
		ConfigurationSerialization.registerClass(FancyMessage.class);
		Chest.registerWithAliases();
		CuboidRegion.registerWithAliases();
		SphericalRegion.registerWithAliases();
		DefaultEnemy.registerWithAliases();
		NormalEnemy.registerWithAliases();
		StandardEnemy.registerWithAliases();
		SimpleSelfSpell.registerWithAliases();
		SimpleTargetSpell.registerWithAliases();
		ChargeSpell.registerWithAliases();
		HealEffect.registerWithAliases();
		DamageEffect.registerWithAliases();
		StatusEffect.registerWithAliases();
		BlockEffect.registerWithAliases();
		AreaEffect.registerWithAliases();
		DamageMPEffect.registerWithAliases();
		SwapEffect.registerWithAliases();
		SummonTamedEffect.registerWithAliases();
		FireEffect.registerWithAliases();
		InvokeSpellWeavingEffect.registerWithAliases();
		DamageUndeadEffect.registerWithAliases();
		CastPylonEffect.registerWithAliases();
		SpellWeavingSpell.registerWithAliases();
		Loot.registerWithAliases();
		ConfigurationSerialization.registerClass(PlayerOptions.class);
		BankStorageManager.registerSerialization();
		BankNPC.registerWithAliases();
		ImbuementSet.registerWithAliases();
		
	}
	
	public Driver() {
		SpellWindow.init();
		initGui();
		openProject = null;
	}
	
	private void initGui() {
		mainWindow = new JFrame(MAIN_TITLE);
		
		toolBar = new JToolBar("toolbar");
		toolBar.setFloatable(false);
		toolBar.setAutoscrolls(false);
		toolBar.setPreferredSize(new Dimension(0,32));
		JButton button = new JButton();
		
		URL iconAddr = null;;
		try {
			iconAddr = ((new File("src/resources/icon_open.png")).toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//URL iconAddr = Driver.class.getResource("icon_open.png");
		
		button.setToolTipText("Open a QuestManager project");
		
		button.setAction(OpenProjectAction.instance());
		if (iconAddr != null)
			button.setIcon(new ImageIcon(iconAddr));
		else 
			button.setText("Open");
		button.setRolloverEnabled(true);
		
		toolBar.add(button);
		mainWindow.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mainWindow.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		//fileViewer = new JScrollPane(new JPanel());
		fileViewer = new JPanel();
		fileViewer.setLayout(new BorderLayout());
		fileViewer.setPreferredSize(new Dimension(200, 400));
		splitPane.setTopComponent(fileViewer);
		explorer = new ProjectExplorer(fileViewer);
		
		
		editorPane = new JDesktopPane();
		editorPane.setPreferredSize(new Dimension(400,400));
		editorPane.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		splitPane.setRightComponent(editorPane);
		this.editor = new Editor(editorPane);
		
		createMenus(mainWindow);
		
		
		mainWindow.setPreferredSize(DEFAULT_SIZE);
		mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainWindow.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				ExitAction.instance().actionPerformed(null);
			}
		});
		mainWindow.pack();
		mainWindow.setVisible(true);
	}
	
	private void createMenus(JFrame frame) {
		MenuBar bar = new MenuBar();
		menus = new Menus(bar);
		frame.setMenuBar(bar);
	}
	
	public JFrame getMainWindow() {
		return this.mainWindow;
	}
	
	public void openProject(File projectFile) {
		if (projectFile == null || !projectFile.exists())
			return;
		
		if (openProject != null)
		if (!editor.closeAllWindows() || !openProject.close())
			return;
		
		explorer.clear();
		openProject = Project.load(projectFile);
	}
	
	public void openProject(Project project) {
		if (project == null)
			return;
		
		if (openProject != null)
		if (!editor.closeAllWindows() || !openProject.close())
			return;
		
		explorer.clear();
		openProject = project;
	}
	
	public ProjectExplorer getExplorer() {
		return explorer;
	}
	
	public Editor getEditor() {
		return editor;
	}
	
	public void saveProject(File saveFile) {
		if (saveFile == null || openProject == null)
			return;
		
		openProject.save(saveFile);
	}
	
	public void saveProject() {
		if (openProject == null)
			return;
		
		openProject.save();
	}
	
	public boolean close() {
		if (openProject != null)
		if (!openProject.close())
			return false;
		
		return true;
	}
	
	public Project getOpenProject() {
		return this.openProject;
	}

}
