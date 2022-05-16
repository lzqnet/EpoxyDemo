



# English Version(From Epoxy Wiki https://github.com/airbnb/epoxy/wiki/Basic-Usage):

> Notice：if Demo report :io.github.lzqnet:lzqfilepicker:1.1.0download fail，you may be try：

> implementation("io.github.lzqnet:lzqfilepicker:1.1.0")

> Change to：

> implementation project(path: ':filepickerlib')





There are two main components of Epoxy:

1. The `EpoxyModel`s that describe how your views should be displayed in the RecyclerView.
2. The `EpoxyController` where the models are used to describe what items to show and with what data.

- Creating Models
  - [Custom Views](https://github.com/airbnb/epoxy/wiki/Basic-Usage#from-custom-views)

- [DataBinding](https://github.com/airbnb/epoxy/wiki/Basic-Usage#from-databinding)

- [View Holders](https://github.com/airbnb/epoxy/wiki/Basic-Usage#from-viewholders)

- [Using Models in a Controller](https://github.com/airbnb/epoxy/wiki/Basic-Usage#controllers)

- [Integrating with RecyclerView](https://github.com/airbnb/epoxy/wiki/Basic-Usage#integrating-with-recyclerview)

### Creating Models

Epoxy generates models for you based on your view or layout. Generated model classes are suffixed with an underscore (`_`) are are used directly in your EpoxyController classes.

#### From Custom Views

Add the `@ModelView` annotation on a view class. Then, add a "prop" annotation on each setter method to mark it as a property for the model.

`@ModelView(autoLayout = Size.MATCH_WIDTH_WRAP_HEIGHT)public class HeaderView extends LinearLayout {`



  `... // Initialization omitted`



  `@TextProp`

  `public void setTitle(CharSequence text) {`

​    `titleView.setText(text);`

  `}`

`}`

A `HeaderViewModel_` is then generated in the same package.

[More Details](https://github.com/airbnb/epoxy/wiki/Generating-Models-from-View-Annotations)

#### From DataBinding

If you use Android DataBinding you can simply set up your xml layouts like normal:

`<layout xmlns:android="http://schemas.android.com/apk/res/android">`

​    `<data>`

​        `<variable name="title" type="String" />`

​    `</data>`



​    `<TextView`

​        `android:layout_width="120dp"`

​        `android:layout_height="40dp"`

​        `android:text="@{title}" />`

`</layout>`

Then, create a `package-info.java` file in any package and add an `EpoxyDataBindingLayouts` annotation to declare your databinding layouts.

@EpoxyDataBindingLayouts({R.layout.header_view, ... // other layouts })

package com.airbnb.epoxy.sample;



import com.airbnb.epoxy.EpoxyDataBindingLayouts;

import com.airbnb.epoxy.R;

From this layout name Epoxy generates a `HeaderViewBindingModel_`.

[More Details](https://github.com/airbnb/epoxy/wiki/Data-Binding-Support)

#### From ViewHolders

If you use xml layouts without databinding you can create a model class to do the binding.

`@EpoxyModelClass(layout = R.layout.header_view)public abstract class HeaderModel extends EpoxyModelWithHolder<Holder> {`

  `@EpoxyAttribute String title;`



  `@Override`

  `public void bind(Holder holder) {`

​    `holder.header.setText(title);`

  `}`



  `static class Holder extends BaseEpoxyHolder {`

​    `@BindView(R.id.text) TextView header;`

  `}`

`}`

A `HeaderModel_` class is generated that subclasses HeaderModel and implements the model details.

[More Details](https://github.com/airbnb/epoxy/wiki/ViewHolder-Models)

### Using your models in a controller

A controller defines what items should be shown in the RecyclerView, by adding the corresponding models in the desired order.

The controller's `buildModels` method declares which items to show. You are responsible for calling `requestModelBuild` whenever your data changes, which triggers `buildModels` to run again. Epoxy tracks changes in the models and automatically binds and updates views.

As an example, our `PhotoController` shows a header, a list of photos, and a loader (if more photos are being loaded). The controller's `setData(photos, loadingMore)` method is called whenever photos are loaded, which triggers a call to `buildModels` so models representing the state of the new data can be built.

`public class PhotoController extends Typed2EpoxyController<List<Photo>, Boolean> {`

​    `@AutoModel HeaderModel_ headerModel;`

​    `@AutoModel LoaderModel_ loaderModel;`



​    `@Override`

​    `protected void buildModels(List<Photo> photos, Boolean loadingMore) {`

​      `headerModel`

​          `.title("My Photos")`

​          `.description("My album description!")`

​          `.addTo(this);`



​      `for (Photo photo : photos) {`

​        `new PhotoModel()`

​           `.id(photo.id())`

​           `.url(photo.url())`

​           `.addTo(this);`

​      `}`



​      `loaderModel`

​          `.addIf(loadingMore, this);`

​    `}`

  `}`

#### Or with Kotlin

An extension function is generated for each model so we can write this:

`class PhotoController : Typed2EpoxyController<List<Photo>, Boolean>() {`



​    `override fun buildModels(photos: List<Photo>, loadingMore: Boolean) {`

​        `header {`

​            `id("header")`

​            `title("My Photos")`

​            `description("My album description!")`

​        `}`



​        `photos.forEach {`

​            `photoView {`

​                `id(it.id())`

​                `url(it.url())`

​            `}`

​        `}`



​        `if (loadingMore) loaderView { id("loader") }`

​    `}`

`}`

### Integrating with RecyclerView

Get the backing adapter off the EpoxyController to set up your RecyclerView:

MyController controller = new MyController();

recyclerView.setAdapter(controller.getAdapter());

// Request a model build whenever your data changes

controller.requestModelBuild();

// Or if you are using a TypedEpoxyController

controller.setData(myData);

If you are using the [EpoxyRecyclerView](https://github.com/airbnb/epoxy/wiki/EpoxyRecyclerView) integration is easier.

epoxyRecyclerView.setControllerAndBuildModels(new MyController());

// Request a model build on the recyclerview when data changes

epoxyRecyclerView.requestModelBuild();

#### Kotlin

Or use [Kotlin Extensions](https://github.com/airbnb/epoxy/wiki/EpoxyRecyclerView#kotlin-extensions) to simplify further and remove the need for a controller class.

`epoxyRecyclerView.withModels {`

​        `header {`

​            `id("header")`

​            `title("My Photos")`

​            `description("My album description!")`

​        `}`



​        `photos.forEach {`

​            `photoView {`

​                `id(it.id())`

​                `url(it.url())`

​            `}`

​        `}`



​        `if (loadingMore) loaderView { id("loader") }`

​    `}`

`}`

### More Reading

And that's it! The controller's declarative style makes it very easy to visualize what the RecyclerView will look like, even when many different view types or items are used. Epoxy handles everything else. If a view only partially changes, such as the description, only that new value is set on the view, so the system is very efficient

Epoxy handles much more than these basics, and is highly configurable. See [the wiki](https://github.com/airbnb/epoxy/wiki) for in depth documentation.

###  



# 中文版

注：如Demo中io.github.lzqnet:lzqfilepicker:1.1.0下载失败，可以将：

implementation("io.github.lzqnet:lzqfilepicker:1.1.0")

改为：

implementation project(path: ':filepickerlib')

# 第一章：基础

Epoxy可帮助您构建复杂的RecyclerView Adapter。 每种Item Type都由`EpoxyModel`表示，它控制屏幕上每个Item的Data和View绑定。 这个Model是从您的自定义View或xml布局生成的。

`EpoxyController`的实现声明了要添加到RecyclerView的Model。 用您的数据创建Model，并按照您希望它们显示的顺序添加它们。

Epoxy相当于一个粘合剂，用于将Model绑定到View上，计算diff以确定哪些数据发生了更改，更新并保存视图状态，以及其他RecyclerView生命周期任务。

## 基本使用

Epoxy有两个重要的组件:

1. `EpoxyModel`，描述了应如何在RecyclerView中显示View
2. `EpoxyController`，Model在这里描述要显示哪些item以及使用哪些数据

- 创建Model
  - 自定义View
  - DataBinding
  - View Holder
- 在Controller中使用Model
- 与RecyclerView集成

### 创建Model

Epoxy根据您的View或Layout为您生成Model。生成的模型类带有下划线（`_`）后缀，可直接在您的EpoxyController类中使用。

#### 从自定义View创建

在View的类名上添加`@ModelView`注解。然后，在每个setter方法上添加“ prop”注解，以将其标记为Model的一个属性。

```
@ModelView(autoLayout = Size.MATCH_WIDTH_WRAP_HEIGHT)

public class HeaderView extends LinearLayout {

  ... // Initialization omitted

  @TextProp

  public void setTitle(CharSequence text) {

    titleView.setText(text);

  }

}
```

然后，同一包下就生成了一个 `HeaderViewModel_`

[更多细节](https://github.com/airbnb/epoxy/wiki/Generating-Models-from-View-Annotations  )

#### 从DataBinding创建

如果您使用Android DataBinding，则可以像通常一样简单地设置xml布局：

```
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable name="title" type="String" />

    </data>

    <TextView

        android:layout_width="120dp"

        android:layout_height="40dp"

        android:text="@{title}" />

</layout>
```

然后，在任何包中创建一个`package-info.java`文件，并添加`EpoxyDataBindingLayouts`注解以声明您的databinding的layout。

```
@EpoxyDataBindingLayouts({R.layout.header_view, ... // other layouts })

package com.airbnb.epoxy.sample;



import com.airbnb.epoxy.EpoxyDataBindingLayouts;

import com.airbnb.epoxy.R;
```

Epoxy从该布局名称生成 `HeaderViewBindingModel_`。

[更多细节](https://github.com/airbnb/epoxy/wiki/Data-Binding-Support  )

#### 从ViewHolder生成

如果使用不带databinding的xml布局，则可以创建一个model类来进行绑定。

```
@EpoxyModelClass(layout = R.layout.header_view)

public abstract class HeaderModel extends EpoxyModelWithHolder<Holder> {

  @EpoxyAttribute String title;



  @Override

  public void bind(Holder holder) {

    holder.header.setText(title);

  }



  static class Holder extends BaseEpoxyHolder {

    @BindView(R.id.text) TextView header;

  }

}
```

生成了一个 `HeaderModel_` 类，该类继承HeaderModel并实现Model的详细信息。

[更多细节](https://github.com/airbnb/epoxy/wiki/ViewHolder-Models  )

### 在Controller中使用Model

Controller通过按顺序添加相应的Model，来定义哪些Item应显示在RecyclerView中。

Controller的`buildModels`方法声明了显示的Item。每当数据更改时，您都需要调用`requestModelBuild`，这会触发`buildModels`重新执行。Epoxy跟踪Model中的更改，并自动绑定和更新View。

例如，我们的`PhotoController`显示了一个Header，一个照片List和一个Loading More的图标（如果正在加载更多照片）。每当加载照片时，都会调用Controller的 `setData(photos, loadingMore)` 方法，这会触发对`buildModels`的调用，然后构建代表新数据状态的Model。

#### `public class PhotoController extends Typed2EpoxyController<List<Photo>, Boolean> {    @AutoModel HeaderModel_ headerModel;    @AutoModel LoaderModel_ loaderModel;     @Override    protected void buildModels(List<Photo> photos, Boolean loadingMore) {      headerModel          .title("My Photos")          .description("My album description!")          .addTo(this);       for (Photo photo : photos) {        new PhotoModel()           .id(photo.id())           .url(photo.url())           .addTo(this);      }       loaderModel          .addIf(loadingMore, this);    }  } `或者用Kotlin

每个Model都会自动生成一个扩展函数，因此我们可以这样写：

### `class PhotoController : Typed2EpoxyController<List<Photo>, Boolean>() {    override fun buildModels(photos: List<Photo>, loadingMore: Boolean) {        header {            id("header")            title("My Photos")            description("My album description!")        }        photos.forEach {            photoView {                id(it.id())                url(it.url())            }        }        if (loadingMore) loaderView { id("loader") }    } } `与RecyclerView集成

从EpoxyController上获得Adapter以设置RecyclerView：

```
MyController controller = new MyController();

recyclerView.setAdapter(controller.getAdapter());



// 每当数据变化时调用requestModelBuild

controller.requestModelBuild();



// 如果你使用TypedEpoxyController的话，调用下面的方法

controller.setData(myData);
```

如果您使用的是[EpoxyRecyclerView](https://github.com/airbnb/epoxy/wiki/EpoxyRecyclerView  )，则集成会更容易。

#### `epoxyRecyclerView.setControllerAndBuildModels(new MyController()); // Request a model build on the recyclerview when data changes epoxyRecyclerView.requestModelBuild(); `Kotlin

或使用[Kotlin Extensions](https://github.com/airbnb/epoxy/wiki/EpoxyRecyclerView#kotlin-extensions)进一步简化并消除对Controller类的需求。

### `epoxyRecyclerView.withModels {        header {            id("header")            title("My Photos")            description("My album description!")        }        photos.forEach {            photoView {                id(it.id())                url(it.url())            }        }        if (loadingMore) loaderView { id("loader") }    } } `更多阅读

就是这样！Controller的声明式使查看RecyclerView的外观变得非常容易，即使使用了许多不同的ViewType或Item也是如此。 Epoxy处理了其他所有事项。 如果视图仅部分更改（例如只更改了描述或者标题），则仅在被更改的View上设置新值，因此非常高效。

Epoxy的处理能力远不止这些基础知识，而且可高度配置。 有关详细文档，请参见[Wiki](https://github.com/airbnb/epoxy/wiki  )。

## 示例App



该示例App演示了具有几种不同类型Model的EpoxyController类。 GridLayoutManager用于允许两列按钮。 EpoxyModelGroups用于将嵌套的水平RecyclerViews与左侧的按钮分组。 有用于添加和随机播放转盘的按钮，并且在每个转盘中都有用于添加，随机播放和更改彩色块的按钮。



Epoxy保存轮播的滚动位置，并管理状态变化的diff以更新RecyclerView并允许动画。



单击彩色块将显示一个星形动画，该动画演示了更改payload的功能。 这使Model可以观察绑定到View时状态的变化。



[Check out 代码！](https://github.com/airbnb/epoxy/tree/master/epoxy-sample/src/main/java/com/airbnb/epoxy  )

# 第二章：核心

## Epoxy Model

### 总览

Epoxy使用 `EpoxyModel` 对象来确定要显示的View以及如何将数据绑定到它们。这类似于流行的ViewModel模式。通过Model，您还可以控制View的其他方面，例如网格span的size，id和保存状态。

### 创建Model

有多种方法可以使Epoxy生成Model类。

- 带注解的自定义View
- 使用Android Databinding
- 使用ViewHolder模式

生成的Model类带有 `_` 后缀表示它们是生成类

### Model ID

稳定的RecyclerView概念已内置在Epoxy中，这意味着每个EpoxyModel应该具有唯一的ID来标识它。这允许比较差异（diff）和保存状态。

通过 `EpoxyModel#id(long)` 方法为Model分配ID。此id值通常来自数据库条目，例如用户的id。

但是，在许多情况下，EpoxyModel没有数据库对象的支持，并且没有可明确分配的ID。在这些情况下，您可以使用字符串作为模型的ID。

```
model.id("header")
```

或者，如果您有来自不同数据库表的对象表示的EpoxyModel，则存在碰撞ID的风险。如果这是一个问题，则可以使用字符串对ID进行命名空间限制。

```
model.id("photo", photoId)

model.id("video", videoId)
```

`id`的其他重载可以接受多个数字或字符串，以帮助您根据需要创建自定义ID。这些替代选项将散列为64位长，以为您创建ID。这种方法的缺点是，由于id是通过散列计算的，因此id冲突的机会会导致错误。但是，由于使用了64位哈希，因此发生冲突的可能性非常小。假设哈希码均匀分布，并且适配器中有数百种模型，则100万亿次碰撞的几率约为1。为了防止冲突，当检测到重复项时，EpoxyController支持回退行为。

#### EpoxyController中的自动ID

对于表示静态内容的模型（例如标题或加载器），可以将[AutoModel注解](https://github.com/airbnb/epoxy/wiki/Epoxy-Controller#automodels  )与EpoxyController一起使用，以自动为您生成唯一的ID。

### Model属性

每个Model都保存最终绑定到View的数据。此数据由属性表示：

- 在自定义View中，在setter上使用`@ModelProp`注释声明属性。
- 在DataBinding布局中，每个变量代表一个模型属性。
- 在视图持有者中，`@EpoxyAttribute`字段注释声明一个属性。

每个属性必须是实现`equals`和`hashcode`的类型。这些实现必须正确识别属性值何时更改。Epoxy依赖于这种diff。如果Epoxy不知道您的属性已更改，则可能不会在View上更新它们。例外情况是回调，例如Click Listener，通常应将 [DoNotHash](https://github.com/airbnb/epoxy/wiki/DoNotHash) 选项应用于该回调。

在生成的EpoxyModels上，每个属性都有一个setter和getter。创建新Model时，可以通过设置每个属性的值来构建它。

```
new MyModel_()

       .id(1)

       .title("title")
```

ID是每个Model的必需属性。

### Model状态

Model的状态由其`equals`和`hashCode`实现确定，该实现基于模型所有属性的值。

此状态用于diff以确定何时更改了Model，以便Epoxy可以更新View。

这些方法是自动生成的，因此您不必手动创建它们。



### 创建View

每个Model都使用其支持的视图进行输入。该模型实现buildView来创建该视图类型的新实例。另外，getViewType返回一个表示该视图类型的int，以用于视图回收。

当Epoxy收到调用 `RecyclerView.Adapter#getItemViewType` 和`RecyclerView.Adapter#onCreateViewHolder` 时，它将委派给Model上的这些方法。

除非您手动创建模型，否则将为您生成这些方法。

### 绑定View

调用 `RecyclerView.Adapter#onBindViewHolder` 时，Epoxy使用 `EpoxyModel#bind(View)`在请求的位置委托给Model。这是Model用属性填充View的机会。

同样，Epoxy将 `RecyclerView.Adapter#onViewRecycled` 委托给`EpoxyModel#unbind(View)`，从而使Model有机会释放与View关联的所有资源。这是清除大型或昂贵数据（例如Bitmap）的View的好机会。

由于RecyclerView在可能的情况下会重用View，因此View可以绑定多次，而不必在两者之间调用取消绑定。您应该确保您对`EpoxyModel#bind(View)` 的使用完全根据模型中的数据更新View。

生成的模型上添加了一个`onBind`方法，您可以使用该方法为Model绑定到View时注册回调。

```
model.onBind((model, view, position) -> // Do something!);
```

同样，`onUnbind`可用于取消绑定回调。

如果您需要在View进入或离开屏幕时采取措施，这些功能将很有用。

### 查看更新

当Model的状态发生变化，并且Model绑定到屏幕上的视图时，Epoxy仅将View被更改的属性重新绑定。这种部分重新绑定比重新绑定整个视图更有效。

如果您使用的是从自定义View或DataBinding生成的Model，那么将自动为您完成部分重新绑定。

如果使用的是手动创建的模型，则可以实现 `EpoxyModel#bind(T view, EpoxyModel<?> previouslyBoundModel)` 以检查哪些属性发生了变化。



注意：这仅适用于`EpoxyController`，而不适用于旧版`EpoxyAdapter`类。



### Click Listener

如果模型的属性类型为 `View.OnClickListener`，则生成的模型将包含一个额外的重载方法，用于设置该Click Listener。重载的方法将带有`OnModelClickListener`参数。

使用两个参数来输入此listener：Model的类型和Model的视图类型。必须实现 `void onClick(T model, V parentView, View clickedView, int position)`。此回调为您提供了被单击View的Model，顶级View（如果使用的是`EpoxyModelWithHolder`，则为ViewHolder），接收到被单击的View以及Model在Adapter中的位置。

您应该使用此Listener提供的位置，而不是在将模型添加到Adapter/Controller时保存对Model位置的引用。这是因为如果Model被移动，RecyclerView会进行优化以不重新绑定新Model，因为它知道数据没有更改。这也意味着，如果不需要将模型重新绑定到视图，则onClick返回的Model不一定是最新创建的Model。

### OnCheckedChangeListener

同样，如果Model的属性类型为 `CompoundButton.OnCheckedChangeListener` ，则将在类型为 `OnModelCheckedChangeListener` 的模型上生成重载方法-在触发Listener时提供对Model和位置的访问。

### 通过注解生成Model

如果使用自定义View，则完全可以基于View中的注解为每个View生成EpoxyModel。

#### 基本使用

首先，先使用`@ModelView`注解自定义View：

```
@ModelView(autoLayout = Size.MATCH_WIDTH_WRAP_HEIGHT)

public class MyView extends FrameLayout {

   ...

}
```

传递给`autoLayout`参数的Size枚举确定在运行时创建View并将其添加到RecyclerView时将哪些值用于布局宽度和高度（WRAP_CONTENT or MATCH_PARENT）。



另外，您可以提供这样的自定义Layout：

```
@ModelView(defaultLayout = R.layout.my_view)
```

如果提供了这样的布局文件，Epoxy将在运行时对其进行inflate以创建View。这使您可以提供自定义样式。布局应仅包含一个child，其类型是您的自定义View。



在这种情况下，我们的布局可能如下所示：

##### `<?xml version="1.0" encoding="utf-8"?> <com.example.MyView xmlns:android="http://schemas.android.com/apk/res/android"    android:layout_width="match_parent"    android:layout_height="wrap_content"    android:background="#ff0000 /> `生成的Model

一旦将此注解添加到View并构建了项目，将在同一包中生成 `MyViewModel_` 类。此类的名称是后缀有 `Model_` 的View的名称，以表示该ViewModel已生成（但是，您可以将生成的类[配置](https://github.com/airbnb/epoxy/wiki/Generating-Models-from-View-Annotations#configuration  )为具有不同的后缀）。



然后可以在您的EpoxyController中使用此生成的Model。

#### 添加属性

生成的Model会为您将数据绑定到您的View上。为此，请在View中注解每个setter方法。

```
@ModelView(defaultLayout = R.layout.my_view)

public class MyView extends FrameLayout {

   ... // Initialization omitted



     @TextProp // Use this annotation for text.

     public void setTitle(CharSequence text) {

       titleView.setText(text);

     }



     @CallbackProp // Use this annotation for click listeners or other callbacks.

     public void clickListener(@Nullable OnClickListener listener) {

       setOnClickListener(listener

     }



     @ModelProp // Use this annotation for any other property types

     public void setBackgroundUrl(String url) {

       ...

     }

}
```

将在生成的Model上为每一个属性注解创建一个setter方法。



当RecyclerView滚动到View展示在屏幕上时，Epoxy将获取Model中每个属性值并将其设置在View上。



如果在屏幕上显示View时Model发生变化，Epoxy将进行高效的部分更新。例如，`MyViewModel_`检查url字符串的值，并且仅在url更改时更新View。如果View上有多个属性并且仅其中一个子集发生更改，则仅更改的属性会更新。



Setter必须仅具有一个参数，返回类型应为`void`，并且不能为静态或私有。



##### 相关属性

每个setter只能使用一个参数，并且在绑定View时，Epoxy无法保证setter的调用顺序。这意味着您不能依赖一个属性去设置另一个属性。



取而代之的是，您可以让setter将其值保存到字段中，然后在方法上使用`@AfterPropsSet` 注解，以在绑定所有属性后调用该方法。然后，您可以使用该方法进行初始化。



您也可以直接使用 `@ModelProp` 注解（非私有）字段，以使Epoxy直接设置该字段值，以避免创建setter的开销。

```
@ModelView(defaultLayout = R.layout.my_view)

public class MyView extends FrameLayout {

   ... // Initialization omitted



     @TextProp CharSequence text;

     @CallbackProp @Nullable OnClickListener clickListener;



     @AfterPropsSet

     void postBindSetup() {

       textView.setText(text);

       textView.setOnClickListener(clickListener);

     }

}
```

如果有需求，我们可能会支持带有多个参数的setter（如果您对此感兴趣，请提一个issue）。

#### String资源

Epoxy will generate methods to use Android String and Plural resources if the `GenerateStringOverloads` option is included in the `@ModelProp` annotation. For example:





字符串资源

如果 `@ModelProp` 注解中包含 `GenerateStringOverloads` 选项，则Epoxy将生成使用Android String和Plural资源的方法。例如：

```
@ModelProp(options = Option.GenerateStringOverloads)

public void setTitle(CharSequence text) {



}
```

或者，为方便起见，可以使用以下内容（其含义相同）

```
@TextProp

public void setTitle(CharSequence text) {



}
```

如果使用此参数，则参数类型必须为`CharSequence`。

生成的Model将具有以下便捷的方法来get和set文本：

#### `  // The getter requires a context to resolve string resources  public CharSequence getTitle(Context context) {    ...  }   public TestViewModel_ title(CharSequence title) {    ...  }   public TestViewModel_ title(@StringRes int stringRes) {    ...  }   public TestViewModel_ title(@StringRes int stringRes, Object... formatArgs) {    ...  }   public TestViewModel_ titleQuantityRes(@PluralsRes int pluralRes, int quantity,      Object... formatArgs) {    ...  } `Setter的注释

ModelProp注解修饰的方法的任何javadoc都将包含在视图模型的生成的setter中。 此外，Model的setter将具有javadoc，用来指定属性是必需还是可选的，默认值是什么以及指向View的setter方法的链接。

#### 属性注解

ModelProp setter参数上包含的所有注解都将包含在生成的Model的方法中。 这对于 `@Nullable`，`@DimenRes`，`@FloatRange`，等注释以及其他Android支持库注释可能会有所帮助。

例如：

#### `  @ModelProp  void setCount(@IntRange(from = 0, to = 10) int value) {   ...  } `可选值和默认值

有一些选项可以指定属性的默认值，以使其成为可选。

##### Kotlin默认参数

（从3.4.0版开始）

指定属性默认值的最简单，最灵活和建议的方法是使用Kotlin并指定默认参数值。

```
    @JvmOverloads

    @ModelProp

    fun color(@ColorInt color: Int = Color.RED) {

        textView.setTextColor(color)

    }
```

如果未在模型上设置显式值，则将使用默认值。



**注意**：使用默认参数值时，必须使用 `@JvmOverloads` 注解该函数。

如果您的属性是必需的，请不要设置默认值。



下面的其余默认选项主要适用于Java，并且复杂得多（并且更旧）。 建议改用Kotlin。

##### 对于对象

默认情况下，属性需要是 `Object` 的子类（即非基本类型）。 通过在setter参数中添加 `@Nullable` 注解，可以使它们成为可选的。 （任何名称为“ Nullable”的注解都可以使用）

```
@ModelProp

public void setImage(@Nullable MyObject param)
```

如果未在Model上指定此属性，则绑定View时此方法将设置为null。

##### 对于原始类型

具有原始类型的属性始终是可选（optional）的。 如果使用Model的用户确实为属性指定了显式值，则它将默认为Java语言指定的原语的默认值。



例如，int将默认为0，而boolean将默认为false。



无法使原始类型的属性成为必需（required）属性。

##### 对于字符串

如果使用 `@TextProp` 注解，则可以指定 `defaultRes` 参数并传递字符串资源以用作默认字符串值。



##### 自定义默认值

如果您希望默认值是自定义的，则可以对对象和原始类型属性使用 `defaultValue` 注解。

```
@ModelProp(defaultValue = "MY_DEFAULT")

public void setImage(MyObject param)
```

字符串 `"MY_DEFAULT"` 表示类中名为 `"MY_DEFAULT"` 的常量。在这种情况下，Epoxy希望View类定义一个像 `static final MyObject MY_DEFAULT = foo;` 这样的字段。该常量必须是static的，final的且不是private的，以便Epoxy可以引用它。常量的类型还必须与属性的类型匹配。



由于对象不是有效的注解参数，因此必须使用常量的名称而不是直接引用常量。



如果属性是一个对象，并且同时具有 `@Nullable` 注解和显式的 `defaultValue` 设置，则行为如下：

1. 如果在Model上未设置属性的值，则将应用自定义 `defaultValue` 
2. 如果将属性值设置为`null`，则将在View上设置null。



#### 属性Group

有时，您可能希望多个setter代表同一个属性。这对于重载的setter很常见，例如，您可以允许通过url或Drawable资源来设置Image。通常，绑定View时，生成的Model始终会设置每个属性，但在这种情况下，同时设置url和Drawable资源将导致其中一个值被覆盖。



解决的办法是告诉Epoxy属性在同一个“组（Group）”中。每组只能设置一个属性。如果Model的使用者为同一组中的多个属性设置值，则仅使用最后设置的属性。



具有相同方法名称的setter将自动放置在同一组中。例如，以下方法签名将导致两个属性放置在同一组中。

```
@ModelProp

public void setImage(String url)



@ModelProp

public void setImage(@DrawableRes int drawableRes)
```

您可以使用`group`参数手动指定组：

```
@ModelProp(group = "image")

public void setImageUrl(String url)



@ModelProp(group = "image")

public void setImageDrawable(@DrawableRes int drawableRes)
```

由于drawableRes是基本类型，因此其默认值为0。因此，如果在Model上未指定Image，则View将使用 `setImageDrawable(0)` 设置默认值。但是，您可以使用标准默认参数为该组指定一个显式默认值：

```
@ModelProp(group = "image")

public void setImageUrl(String url)



@ModelProp(group = "image", defaultValue = "DEFAULT_DRAWABLE_RES")

public void setImageDrawable(@DrawableRes int drawableRes)
```

每个组只能有一个属性可以指定一个明确的默认值，否则将在编译时引发异常。



如果一个组中有多个基本类型属性，并且未设置任何显式默认值，则绑定视图时，将使用其Java默认值来设置一个基本属性。（未定义选择哪个属性）。



如果组中的一个属性在其参数上具有 `@Nullable` 注解（并且未设置任何显式默认值），则该属性将设置为null作为组的默认值。



如果组中没有原始类型属性，并且没有属性是 `@Nullable` 的，则需要为组设置一个值。



如果组中有原始类型属性，则无法为该组设置必需（required）值，因为原始类型（尚）不支持必需（required）值。

#### 从Model State中排除属性

每个属性类型都必须实现 `equals` 和 `hashCode` 。这是必要的，以便Epoxy可以确定何时更改Model的状态，以便知道何时更新View。如果类型没有提供 `Object` 类的默认实现以外的 `equals` 和 `hashCode` 实现，则在编译时将引发错误。



但是，某些常见类型（例如接口）不实现 `equals` 和 `hashCode` 。一个常见的示例是 `View.OnClickListener` 。在ClickListener的情况下，Listener不太可能需要改变Model状态。重建Model时，通常会为Listener创建一个新的匿名类，但通常具有相同的实现。这是一种非常常见的模式，通常，仅当Listener从未设置变为设置（反之亦然）时，才需要更新View。



为此，您可以使用 `DoNotHash` 选项。

```
  @ModelProp(options = Option.DoNotHash)

  public void setClickListener(@Nullable View.OnClickListener listener) {



  }
```

Epoxy不会在Listener对象上使用 `equals` 和 `hashCode` 方法来计算Model的状态。但是，Listener的存在将更改模型状态。也就是说，如果Listener从null变为非null，则Epoxy将使用新的Listener更新View。



考虑这一点的一种好方法是：“如果我的View在屏幕上，并且属性的hashCode改变了，我是否要相应地更新View？”如果是这样，请确保对象的类型正确实现 `equals` 和 `hashCode` ，或者可以使用`DoNotHash`。

##### IgnoreRequireHashCode

有时，您的属性必须是尚未实现 `equals` 或 `hashCode` 的一种接口或抽象类。但是，您可能知道在运行时该类型将实现equals和hashCode。在这种情况下，您仍然希望Epoxy使用 `equals` 和 `hashCode` 来允许属性修改模型状态，但是由于属性的编译时类型不能实现它们，因此Epoxy将引发错误。



为了解决这个问题，您可以使用选项 `IgnoreRequireHashCode` 向Epoxy表示您知道equals和hashCode实现丢失了，但是可以忽略并继续使用它们。

```
  @ModelProp(options = Option.IgnoreRequireHashCode)

  public void setObject(MyInterface myInterface) {

    ...

  }
```

一个示例情况是使用 `@AutoValue` 生成对象，其中仅生成的类实现了equals和hashCode。（由于AutoValue是一种常见情况，如果Epoxy看到带有注释的类型，则实际上将允许该类型不具有equals和hashCode。）



另外，也可以不使用`IgnoreRequireHashCode`，而只需在接口或抽象类中添加 `equals` 和 `hashCode` 的抽象stub，以告诉Epoxy属性类型在运行时实现将正确实现这些方法。



#### 回收View

当View从屏幕上滚动出去时，该View将脱离其Model并由RecyclerView回收。如果您此时希望清理资源，则可以使用 `@OnViewRecycled` 在View中注解一个或多个方法。

```
@OnViewRecycled

public void clear(){

   ...

}
```

解绑和回收View时，将调用带有此注解的任何方法。这是释放Listener（以防止内存泄漏）或释放Bitmap之类的大对象以释放内存的好时机。您也可以使用它来取消正在运行的操作。



具有此批注的方法不得为private或static，并且不能具有任何参数。



##### 自动清除Nullable对象

如果使用 `@Nullable` 注释属性，则可以在属性的注解上设置 `NullOnRecycle` 选项，以告知Epoxy在回收View时使用空值调用setter。

```
  @ModelProp(options = Option.NullOnRecycle)

  public void setTitle(@Nullable CharSequence title) {

    ...

  }
```

这是使用 `OnViewRecycled` 手动执行此操作的快捷方式，例如：

```
@OnViewRecycled

public void clear(){

   setTitle(null);

}
```

两种方法都是等效的，但是 `NullOnRecycle` 选项是您要清除的可空属性的不错的快捷方式。这对于诸如Listener或Image之类的对象可能会有所帮助。



##### 回调类型的属性

如果要在视图上设置Listener或回调，则可以使用 `@CallbackProp` 注解而不是 `@ModelProp` 来自动应用通常与侦听器一起使用的 `NullOnRecycle` 和 `DoNotHash` 选项。



我们这么写：

```
@CallbackProp

public void setListener(@Nullable OnClickListener clickListener) {



}
```

而不是更冗长的写法：

```
@ModelProp(options = {Option.NullOnRecycle, Option.DoNotHash})

public void setListener(@Nullable OnClickListener clickListener) {



}
```

这也有助于强制执行以下要求：在回收View时清除所有Listener，以免泄漏。

这只能用于标记为可空的参数。



**重要提示**：由于已应用 `DoNotHash` ，Epoxy不知道您的回调函数的实现是否已更改，因此您无法对其进行更改。[阅读更多](https://github.com/airbnb/epoxy/wiki/DoNotHash  )



#### 保存状态

如果生成的Model应保存View状态（在[此处](https://github.com/airbnb/epoxy/wiki/Saving-View-State)说明），则可以在 `ModelView` 注解中将 `saveViewState` 参数设置为true。

```
@ModelView(defaultLayout = R.layout.my_layout, saveViewState = true)

public class MyView extends View {

   ...

}
```

然后，生成的Model将包含此Override

```
@Override

  public boolean shouldSaveViewState() {

    return true;

  }
```

否则 `shouldSaveViewState` 将默认为false。



#### Grid Span Size

默认情况下，与GridLayoutManager一起使用时，生成的Model将占用全部span count（有关更多详细信息，请参见[Grid支持](https://github.com/airbnb/epoxy/wiki/Grid-Support)）。如果您希望自定义它们占用的span，则可以在 `ModelView` 注解中将 `fullSpan` 参数设置为false。

```
@ModelView(defaultLayout = R.layout.my_layout, fullSpan = false)

public class MyView extends View {

   ...

}
```

如果您希望对Model的span size进行更精细的控制，则可以在EpoxyController中使用Model时在Model上设置 `SpanSizeOverrideCallback` 。

#### `model.spanSizeOverride(new SpanSizeOverrideCallback() {      @Override      public int getSpanSize(int totalSpanCount, int position, int itemCount) {        return totalSpanCount / 2;      }    }); `Base Model

默认情况下，为 `@ModelView` 的View生成的Model将继承 `EpoxyModel` 。但是，您可以根据需要指定自定义基类。

```
@ModelView(defaultLayout = R.layout.my_view, baseModelClass = CustomBaseModel.class))

public class MyView extends FrameLayout {

   ...

}
```

Base Model必须继承EpoxyModel，并且可能是这样的：

```
public abstract class CustomBaseModel<T extends View> extends EpoxyModel<T> {

   @EpoxyAttribute protected boolean baseModelBoolean;



  @Override

  public void bind(T view) {

    ...

  }

}
```

Base Model可以定义EpoxyAttributes，这将使生成的Model包括setter，getter和equals / hashCode函数。但是，Base Model有必要根据需要将属性绑定到View。



如果您希望所有View都使用相同的Base Model，则可以设置package默认值。有关更多详细信息，请参见[配置](https://github.com/airbnb/epoxy/wiki/Generating-Models-from-View-Annotations#configuration)。



如果设置了package默认值，您仍然可以通过在View中显式指定Base Model来覆盖它。



#### View实现的接口

*从2.7.0开始*



如果您的View实现了一个接口，并且至少一个接口方法的实现使用prop注解，则生成的模型将实现一个表示该View接口的新接口。



这使得生成的Model可以利用多态性。您可以将它们分组并通过它们的通用接口设置数据。



##### 例

假设一个View实现了此接口，并且setClickable实现由 `@ModelProp` 注解。

```
interface Clickable {

  void setClickable(boolean clickable);

  boolean isClickable();

}
```

将生成一个新的 `ClickableModel_` 接口，并且生成的model将实现它。该接口将仅包括setter，而不包括getter。

```
interface ClickableModel_ {

 ClickableModel_ clickable(boolean clickable);

 ClickableModel_ id(long id);

 // other standard model methods

}
```

然后，您可以将不同的Model转换为 `ClickableModel_` 并轻松访问共享的属性。



##### 细节

生成的接口不一定和原始View的接口一模一样。而是，Epoxy使用该View类型查看所有生成的Model，获取它们都共享的属性的子集，并使生成的接口实现这些属性的方法。



生成的接口与View的接口位于同一包中。



生成的接口是带有 `Model_` 作为后缀的原始接口的名称。如果接口声明嵌套在另一个类中，则生成的接口名称将以顶级类的名称为前缀，例如 `TopLevelClass_InterfaceNameModel_` 。



##### 注意事项

- 不支持View继承接口
- 在具有相同接口的其他模块中不支持 `@ModelViews`
- 不支持部分实现接口的抽象View

如果需要，可以在将来的版本中解决这些问题。如果您需要支持，请提一个issue：）



#### 配置

`PackageModelViewConfig` 程序包注解允许定义程序包范围的配置设置。这些设置也适用于嵌套包，除非嵌套包定义了自己的 `PackageModelViewConfig` 设置。



要使用此注解，请将 `package-info.java` 文件添加到包含您的View的包中，并使用 `PackageModelViewConfig` 对其进行批注。



例如：

```
@PackageModelViewConfig(rClass = R.class)

package com.example.app;



import com.airbnb.epoxy.PackageModelViewConfig;

import com.example.app.R;
```

注释唯一需要的参数是 `rClass` ，它定义模块的R类。



##### 可用选项

###### 默认BaseModel

您可以使用可选的 `defaultBaseModelClass` 参数为包中的每个视图指定默认[BaseModel](https://github.com/airbnb/epoxy/wiki/Generating-Models-from-View-Annotations#Base-Models  )。



###### 生成的Model后缀

我的默认生成的模型类具有后缀 `Model_` 。您可以使用generateModelSuffix参数将此后缀更改为所需的任何名称。



###### 默认布局名称

`defaultLayoutPattern` 参数定义了每个View所需的布局命名方案，以便该View的 `@ModelView`注解可以省略 `defaultLayout` 参数。



`defaultLayoutPattern` 接受任何包含`％s`的字符串。 `％s`表示下划线格式名称情况下给定View的名称。



例如：

```
@PackageModelViewConfig(rClass = R.class, defaultLayoutPattern = "view_holder_%s")

package com.example.app;
```

结合View

```
@ModelView

public class MyView extends FrameLayout {

   ...

}
```

会生成一个使用名为 `R.layout.view_holder_my_view` 的布局的Model。



这等效于使用以下命令手动定义布局

```
@ModelView(defaultLayout = R.layout.my_view)

public class MyView extends FrameLayout {

   ...

}
```

用于提供布局模式的软件包设置有助于从每个View中省略代码，还有助于标准化软件包中的命名约定和期望。



**注意** 您仍然必须手动创建布局文件以声明View的样式和布局参数。此设置仅使您节省将View与其适当的布局链接的步骤。



###### 布局重载

注意：Kotlin类需要使用替代方法来使用它。请参阅下面的更多细节



将 `useLayoutOverloads` 设置为true可使Epoxy生成用于更改View使用的布局文件的Model方法。这些称为“布局重载”。



如果启用此选项，则Epoxy将查找名称以View的默认布局名称开头的布局文件。例如

```
@ModelView(defaultLayout = R.layout.my_view)

public class MyView extends FrameLayout {

   ...

}
```

将具有默认布局 `R.layout.my_view` ，并且以 `my_view_` 开头的任何其他布局文件将被视为为MyView生成的Model的重载。



这对于通过Model将不同样式应用于View特别有用。



因此，对于此示例，我们可以这样创建布局文件 `R.layout.my_view_blue` ：

```
<?xml version="1.0" encoding="utf-8"?>

<com.example.MyView xmlns:android="http://schemas.android.com/apk/res/android"

    android:layout_width="match_parent"

    android:layout_height="wrap_content"

    android:background="@color/blue />
```

启用布局重载后，`MyViewModel_` 将生成以下方法：

```
  @Override

  @LayoutRes

  protected int getDefaultLayout() {

    return R.layout.my_view;

  }



  public MyViewModel_ withBlueLayout() {

    layout(R.layout.my_view_blue);

    return this;

  }
```

默认情况下，它使用相同的 `R.layout.my_view` 布局，但是我们现在可以选择调用`myViewModel.withBlueLayout()` 从 `R.layout.my_view_blue` 创建一个新View以应用蓝色样式。



请注意，由于Epoxy使用布局文件作为item的ViewType，因此从不同布局文件创建的View将不会一起回收。

Kotlin的布局重载

如果在ModelView注解中提供布局，例如 `@ModelView(defaultLayout = R.layout.my_view)`，则用Kotlin编写的View不能使用布局重载。因为很不幸，布局名称未保留在注解处理器中，因此Epoxy无法访问它。



但是，如果使用[默认布局名称](https://github.com/airbnb/epoxy/wiki/Generating-Models-from-View-Annotations#default-layout-names  )，则仍然可以使用布局重载。



设置软件包信息配置注解以启用布局重载并指定R类

```
@PackageModelViewConfig(rClass = R.class, useLayoutOverloads = true)

package com.example.app;
```

然后创建没有布局的视图类

```
@ModelView

public class MyView extends FrameLayout {

   ...

}
```

Epoxy将自动查找名为 `R.layout.my_view` 的布局。以 `my_view` 开头的任何其他布局文件都将作为布局重载包含在生成的Model中。

### 通过Data Binding生成Model

Epoxy与[Android data binding](https://developer.android.com/topic/libraries/data-binding/index.html)集成，可从数据绑定xml布局自动生成EpoxyModels。



要启用Data Binding支持，请添加epoxy databinding模块作为依赖项：

```
dependencies {

  compile 'com.airbnb.android:epoxy-databinding:2.6.0' // (or latest Epoxy version)

}
```

另外，请确保在build.gradle文件中启用Android Data Binding。

#### `android {  dataBinding {    enabled = true  } } `自动模型生成

Epoxy可以为您的每个Data Binding布局生成一个EpoxyModel。

Model是在模块的根包中生成的。每个模型都将为布局中的每个变量设置一个setter，并且在绑定模型时将绑定每个变量。

生成的模型名称由驼峰式布局文件名并附加`BindingModel_`创建。布局不得通过布局xml中的 `class="com.example.CustomClassName"` 重写指定自定义数据绑定类名称或包。

##### 声明数据绑定布局

有两种方法可以告诉Epoxy要为其生成Model的布局：

###### 基于命名模式自动声明

在任何包中创建一个package-info.java文件，并使用EpoxyDataBindingPattern对其进行注释。添加您的R类作为参数，以及所有数据绑定布局共享的前缀。

Epoxy将尝试为每个以给定前缀开头的布局文件生成一个数据绑定模型。

例如，

```
@EpoxyDataBindingPattern(rClass = R.class, layoutPrefix = "view_holder")

package com.example.package;
```

然后，您的布局将命名为`view_holder_list_item.xml`，这将导致生成`ListItemBindingModel_`模型类。

###### 明确声明

在任何包中创建一个`package-info.java`文件，并使用`EpoxyDataBindingLayouts`对其进行注释。然后在注释中包含布局文件名。

例如，

##### `@EpoxyDataBindingLayouts({R.layout.header_view}) package com.example.package; `检测数据变化

通常，所有数据变量都必须实现equals和hashcode，以便Epoxy可以检测到它们何时更改。每个变量在更改时都会分别重新绑定。在“[模型属性](https://github.com/airbnb/epoxy/wiki/Epoxy-Models#model-properties  )”部分下阅读有关此内容的更多信息。

对于任何未实现equals和hashcode的变量，默认情况下，Epoxy将应用其“不哈希”选项。通常，此默认设置对Click Listener很有帮助，但是应避免某些行为，以免出现错误。

要禁用此默认设置，可以将`EpoxyDataBindingLayouts`和`EpoxyDataBindingPattern`注解中的`enableDoNotHash`设置为false。

#### 自定义数据绑定模型

如果要手动处理绑定，也可以直接继承`DataBindingEpoxyModel`，尽管很少需要这样做。



您可以创建一个简单的Model，如下所示：

```
@EpoxyModelClass(layout = R.layout.model_button)

public abstract class ButtonModel extends DataBindingEpoxyModel {

  @EpoxyAttribute @StringRes int textRes;

  @EpoxyAttribute(DoNotHash) OnClickListener clickListener;



  @Override

  protected void setDataBindingVariables(ViewDataBinding binding) {

    binding.setVariable(BR.textRes, textRes);

    binding.setVariable(BR.clickListener, clickListener);  }

}
```

绑定模型时，将调用`setDataBindingVariables`方法，从而使您有机会将数据绑定到视图。另外，您可以不使用`setDataBindingVariables`方法，而Epoxy将为您提供一个默认实现，该实现将每个EpoxyAttribute字段绑定到同名变量。



这将Model简化为：

```
@EpoxyModelClass(layout = R.layout.model_button)

public abstract class ButtonModel extends DataBindingEpoxyModel {

  @EpoxyAttribute @StringRes int textRes;

  @EpoxyAttribute(DoNotHash) OnClickListener clickListener;

}
```

在这种情况下，生成的`setDataBindingVariables`实现将与之前实现的完全相同。生成的代码还将包括`setDataBindingVariables`实现，该实现仅在变量从先前绑定的模型更改后才更新。



例如：

```
@Override

protected void setDataBindingVariables(ViewDataBinding binding, EpoxyModel previousModel) {

    ButtonModel_ that = (ButtonModel_) previousModel;

    if (textRes != that.textRes) {

      binding.setVariable(BR.textRes, textRes);

    }

    if ((clickListener == null) != (that.clickListener == null)) {

      binding.setVariable(BR.clickListener, clickListener);

    }

}
```

### 通过ViewHolder生成Model

如果您不使用Data Binding或自定义View，则可以使用ViewHolder为布局创建EpoxyModel。

#### 使用ViewHolder

创建一个继承`EpoxyModelWithHolder`的类。模型类的泛型类型应该使用您的ViewHolder类型。



ViewHolder类必须继承`EpoxyHolder`；可以在任何位置定义它，但是一个好的模式是使其成为Model的嵌套类。



```
@EpoxyModelClass(layout = R.layout.model_button)

public abstract class ButtonModel extends EpoxyModelWithHolder<Holder> {



  // Declare your model properties like this

  @EpoxyAttribute @StringRes int text;

  @EpoxyAttribute(DoNotHash) OnClickListener clickListener;



  @Override

  public void bind(Holder holder) {

    // Implement this to bind the properties to the view

    holder.button.setText(text);

    holder.button.setOnClickListener(clickListener);

  }



  static class Holder extends EpoxyHolder {

    Button button;



    @Override

    protected void bindView(View itemView) {

      button = itemView.findViewById(R.id.button);

    }

  }

}
```

构建项目后，将生成一个`ButtonModel_`类，您可以在您的`EpoxyControllers`中使用该类。

生成的模型应该直接实例化，并且每个属性都有一个setter：

```
new ButtonModel_()

   .id(1)

   .text(R.string.my_text)

   .clickListener(() -> // do something);
```

注意事项：

- `@EpoxyModelClass(layout = R.layout.model_button)` 定义将被inflate的布局。
- 让您的Model类是抽象的。不应直接使用它-而是应实例化`ButtonModel_`生成的类，并在您的`EpoxyController`中使用它。
- 通过使用`@EpoxyAttribute`注释字段来定义模型属性。所生成类的setter将为我们设置这些字段。您永远不要直接设置这些字段。
- 将Model绑定到View时，将调用`bind`方法，该方法应用于在视图持有者的视图上设置数据。
- ViewHolder类仅持有对View的引用。当View被inflate时，`bindView`被调用一次。



##### BaseHolder模式

一个有用的模式是创建一个基类，您的app中的所有ViewHolder都可以扩展该基类。您的基类可以使用ButterKnife绑定其视图，这样子类就无需明确地需要这样做。



例如，您的基类可能如下所示：

```
public abstract class BaseEpoxyHolder extends EpoxyHolder {

  @CallSuper

  @Override

  protected void bindView(View itemView) {

    ButterKnife.bind(this, itemView);

  }

}
```

应用此模式有助于将我们的示例模型简化为：

```
@EpoxyModelClass(layout = R.layout.model_button)

public abstract class ButtonModel extends EpoxyModelWithHolder<Holder> {

  @EpoxyAttribute @StringRes int text;

  @EpoxyAttribute OnClickListener clickListener;



  @Override

  public void bind(Holder holder) {

    holder.button.setText(text);

    holder.button.setOnClickListener(clickListener);

  }



  static class Holder extends BaseEpoxyHolder {

    @BindView(R.id.button) Button button;

  }

}
```

#### 使用Kotlin

[Wiki的Kotlin部分](https://github.com/airbnb/epoxy/wiki/Kotlin-Model-Examples#with-view-holders  )中提供了Kotlin特定的Viewholder模式。



## Epoxy Controller

### 原理

EpoxyController鼓励使用类似于流行的Model-View-ViewModel和Model-View-Intent模式的用法。

数据沿一个方向流动：从您的State到EpoxyModel，再到RecyclerView上的View。

用户对View的输入会触发回调，这些回调可能会更新您的State并重新启动周期。

Epoxy会影响您的State与EpoxyModel，EpoxyModel和View之间的接口。如何管理State和查看回调的方法由您决定。

EpoxyModel类似于ViewModel。它是一个不变的类，是数据和View之间的接口。它格式化数据并相应地更新View。该视图无法修改EpoxyModel，而必须回调以更改您的State，以便可以创建新的EpoxyModel。

这与EpoxyAdapter不同，后者的EpoxyModels是可变的，可以更新和重用。该模式可能是不可预测的并且容易出错，并且在EpoxyController中是不允许的。

### 用法

继承EpoxyController并实现 `buildModels` 方法。此方法的目标是构建代表在调用该方法时数据状态的 [EpoxyModel](https://github.com/airbnb/epoxy/wiki/Epoxy-Models)。生成的Model是不可变的，并且被Epoxy用于在RecyclerView中创建和绑定View。当再次调用 `buildModels` 时，Epoxy将最新Model与以前的Model进行比较，以确定需要对RecyclerView进行哪些更新。

您的 `buildModels` 实现应创建EpoxyModels的新实例（或使用[AutoModel](https://github.com/airbnb/epoxy/wiki/Epoxy-Controller#auto-models)），在其上设置适当的数据，然后按照应在RecyclerView中显示的顺序添加它们。可以通过调用 `EpoxyController#add(EpoxyModel)` 或 `EpoxyModel#addTo(EpoxyController)` 来添加它们。

要使用您的Controller，请创建一个新实例并调用 `getAdapter()` 以获取要在RecyclerView上设置的Adapter。然后，在Controller上调用 `requestModelBuild`，以告诉Epoxy触发model重建和更新Adapter（您不能直接调用 `buildModels`）。每当您的数据更改并且您希望相应地更新RecyclerView时，再次调用 `requestModelBuild`。

#### 例

```
public class PhotoController extends EpoxyController {

   private List<Photo> photos = Collections.emptyList();

   private boolean loadingMore = true;



   public void setLoadingMore(boolean loadingMore) {

      this.loadingMore = loadingMore;

      requestModelBuild();

   }



   public void setPhotos(List<Photo> photos) {

      this.photos = photos;

      requestModelBuild();

   }



    @Override

    protected void buildModels() {

      new HeaderModel_()

          .id("header model")

          .title("My Photos")

          .addTo(this);



      for (Photo photo : photos) {

        new PhotoModel_()

              .id(photo.getId())

              .url(photo.getUrl())

              .comment(photo.getComment())

              .addTo(this);

      }



      new LoadingModel_()

          .id("loading model")

          .addIf(loadingMore, this);

    }

}
```

Controller的设置如下：

```
controller = new PhotoController();

recyclerView.setAdapter(controller.getAdapter());

controller.requestModelBuild();
```

然后，您可以想象我们可能会发出多个网络请求来加载照片，并且每次更改时都会调用带有更新后的照片列表的 `controller.setPhotos` ，而当我们没有更多照片要加载时，将调用 `controller.setLoading(false)` 。

请注意，这两个setter都调用 `requestModelBuild` 来通知Epoxy应该重建Model。您可以选择在Controller内部还是外部进行此调用。同样，在此示例中，我们将数据存储为字段并添加setter方法以对其进行更新，但是EpoxyController不需要存储或访问`buildModels`中使用的数据的方式。这种灵活性使EpoxyController可以适应您遵循的任何架构模式。但是，如果您想要更结构化的用法，可以使用[TypedEpoxyController](https://github.com/airbnb/epoxy/wiki/Epoxy-Controller#typed-controllers  )。

#### 建立Model的细节

为了使Controller正常工作，Epoxy希望Model遵循两个重要规则：

- 所有模型都必须设置[唯一的ID](https://github.com/airbnb/epoxy/wiki/Epoxy-Models#model-ids  )。
- Model是不可变的，一旦将其添加到控制器，就无法更改其[状态](https://github.com/airbnb/epoxy/wiki/Epoxy-Models#model-state  )。 （一个例外是[拦截器](https://github.com/airbnb/epoxy/wiki/Epoxy-Controller#interceptors  )）

这些规则对于使Diff正常工作是必需的，以便使每个View都与数据状态保持一致。Epoxy通过运行时[检查](https://github.com/airbnb/epoxy/wiki/Epoxy-Controller#validations  )来强制执行这些规则。

实例化Model时，不允许使用分配给它们的默认ID。这仅在EpoxyAdapter中允许。 EpoxyController中的每个Model都必须在其上设置一个明确的ID或为AutoModel。

`requestModelBuild` 顾名思义，它要求构建Model，但不能保证会立即发生。第一次在控制器上调用该Model时，将立即构建Model（以便尽快填充View并可以恢复View状态），但随后的调用将被post和debounce频控。这是为了将Model构建与数据更改脱钩。这样，所有数据更新都可以完全完成，而不必担心多次调用 `requestModelBuild` 。

例如，在我们的PhotoController示例中，如果我们同时更改加载状态和照片列表，则不想触发两次Model构建。通过post和debounce，我们可以安全地调用 `requestModelBuild` 两次（每个setter一次），并且Model将只构建一次。这使调用代码不必尝试优化对 `requestModelBuild` 的调用。

每个 `buildModels` 调用都完全独立于先前的调用。`buildModels` 始终以空的Modle列表开头，并且必须创建，修改和添加当时表示数据的所有Model。

您可以使用 `EpoxyModel#addIf` 代替常规的add方法有条件地添加模型。在EpoxyController中，不允许使用 `EpoxyModel#hide()` 隐藏模型。

#### Adapter和Diff细节

构建Model后，Epoxy会在Adapter上设置新Model，并运行[Diff](https://github.com/airbnb/epoxy/wiki/Diffing  )算法以针对先前Model列表计算更改。任何项目更改都会通知给RecyclerView，以便可以根据需要删除，插入，移动或更新View。

Epoxy为您管理所有View的创建，绑定和回收。View绑定调用被[委派给Model](https://github.com/airbnb/epoxy/wiki/Epoxy-Models#binding-models  )，以便Model可以使用其表示的数据更新View。其他View生命周期事件（例如，attach/detach到Window和View回收）也被委托给Model。

### 异步支持

EpoxyController有次级构造函数，它接受两个 `Handler` 实例，一个实例用于运行Model构建，另一个实例用于处理Diff。默认情况下，它们使用主线程，但是可以更改它们以允许异步工作以提高性能。



**注意**：EpoxyController第一次构建Model时，即使您已指定异步处理程序，它也将始终在主线程上运行。为了使View在首次创建时能够同步还原保存的状态，这是必需的。更多详细信息，请参见 `EpoxyController#requestModelBuild` 上的Javadoc。



要轻松使用此功能，可以继承 `AsyncEpoxyController` 。如果您希望控制Epoxy使用的线程，请阅读下面的内容。

可以通过设置静态字段 `EpoxyController#defaultDiffingHandler` 和`EpoxyController#defaultModelBuildingHandler` 来更改默认处理程序，这些静态字段可让您强制应用程序中所有Controller的异步行为。

例如，您可以像这样将全局默认值设置为异步

```
    HandlerThread handlerThread = new HandlerThread("epoxy");

    handlerThread.start();

    Handler handler = new Handler(handlerThread.getLooper());

    EpoxyController.defaultDiffingHandler = handler;

    EpoxyController.defaultModelBuildingHandler = handler;
```

使用Handler是因为Epoxy需要能够轻松地将主线程作为默认线程。另外，Epoxy不支持并行Model构建，而Handler通过其单线程循环行为来强制执行此操作。

请注意，如果您使用异步Model构建，则必须确保所有数据访问都是线程安全的-Epoxy不会对此提供帮助。这意味着您的 `EpoxyController#buildModels` 实现在所有数据访问中都是线程安全的。

如果为Model构建和Diff提供了相同的Handler，则所有工作将从模型构建开始到差异化结束在同一个线程上同步进行。



### 监听Model变更

如果您希望在Model完成构建和Diff以及更新RecyclerView时收到警告，则可以使用 `EpoxyController#addModelBuildListener` 方法注册回调。

使用此功能可以响应通知RecyclerView之后需要进行的Model更改，例如滚动。

### 验证

如上所述，EpoxyController希望每个模型都有唯一的ID，并且在将其添加到Controller后不会被修改（一个例外是[Interceptors](https://github.com/airbnb/epoxy/wiki/Epoxy-Controller#interceptors  )）。违反此规定将导致运行时异常。

这些验证对于帮助您避免使用Epoxy时出错是非常重要的。默认情况下启用它们，但[可以禁用它们](https://github.com/airbnb/epoxy/wiki/Configuration  )。您可能希望针对对外版本禁用它们，以避免验证检查的运行时开销。

验证的主要目的是增强Model的不变性。不允许任何导致更改Model的hashCode的行为。这可能是一个容易犯的错误，因为Model没有由编译器强制执行其不变性。也就是说，EpoxyModels的常规模式不使用final字段和builder模式，因此编译器仍将允许更改字段。通过定期断言每个模型的hashCode从首次添加时起没有发生变化，就可以使用运行时验证来警告任何意外违规。

### AutoModel

如果Adapter中始终存在Model（例如标题或加载器），则可以将它们标记为带有 `@AutoModel` 注释的字段，以使Epoxy自动为您创建Model并分配唯一的ID。该ID在不同的Adapter实例中将是稳定的，因此可用于保存旋转状态下的模型状态。

**注意**：这不适用于 `PagedListEpoxyController`，如果使用Kotlin，则不建议这样做。有关Kotlin的建议用法，请参阅 [Kotlin用法](https://github.com/airbnb/epoxy/wiki/Epoxy-Controller#usage-with-kotlin)

例如，我们可以像这样从上方更新照片控制器：

```
public class PhotoController extends EpoxyController {

   @AutoModel HeaderModel_ header;

   @AutoModel LoadingModel_ loader;



   private List<Photo> photos = Collections.emptyList();

   private boolean loadingMore = true;



   public void setLoadingMore(boolean loadingMore) {

      this.loadingMore = loadingMore;

      requestModelBuild();

   }



   public void setPhotos(List<Photo> photos) {

      this.photos = photos;

      requestModelBuild();

   }



    @Override

    protected void buildModels() {

      header

          .title("My Photos")

          .addTo(this);



      for (Photo photo : photos) {

        new PhotoModel_()

              .id(photo.getId())

              .url(photo.getUrl())

              .comment(photo.getComment())

              .addTo(this);

      }



      loader

          .addIf(loadingMore, this);

    }

}
```

AutoModel字段会自动为您创建；在每个buildModels调用之前创建一个新实例。您永远不要手动将值分配给AutoModel字段，或者在buildModels方法之外修改它们。另外，请记住，由于始终都会重新创建Model，因此您不能使用==将字段值与Adapter上的模型进行比较（例如，在onBind回调中）。

生成的ID始终为负值，因此它们与手动设置的ID（例如，数据库中的ID通常大于0）冲突的可能性较小。

#### 隐式添加

（在2.1版本中可用）



如果[启用](https://github.com/airbnb/epoxy/wiki/Configuration)，使用AutoModel注解创建的Model在 `buildModels` 方法中修改后将自动添加到Controller中。这样，您可以在构建模型后省略addTo方法调用。默认情况下禁用。

有一些规则来控制此过程。首先，一旦在buildModels方法中修改了AutoModel（即，调用了任何setter方法以更新其数据），就会对其进行“暂存”以进行隐式添加。暂存模型后，一旦修改或添加了另一个模型（或 `buildModels` 方法返回），它将自动添加到Controller中。

如果Model未通过 `addIf` 条件，则该Model将从暂存中删除。如果使用`addIf`，`addTo`或 `add(...)` 手动添加已暂存的Model，则它将从暂存中删除并且不会被添加两次。

如果您不需要修改模型（例如加载器），则无法暂存模型以进行隐式添加，因此您必须像普通模型一样手动添加- `model.addTo(...)` 或 `add(model)` 。

如果您有许多AutoModel，则此隐式添加可能是删除样板的好方法。下面是一个示例，说明了如果我们从上方更新示例代码以使用隐式添加，将如何显示：

```
@Override

    protected void buildModels() {

      header

          .title("My Photos");

          // No "addTo" call is needed here. The model will automatically be added.

      for (Photo photo : photos) {

        new PhotoModel_()

              .id(photo.getId())

              .url(photo.getUrl())

              .comment(photo.getComment())

              .addTo(this);

      }



      loader

          .addIf(loadingMore, this);

    }
```

#### 内部类的用法

AutoModel注解不适用于内部类中的字段（静态嵌套类工作正常）。如果要将EpoxyController用作嵌套类（以便它可以访问其父类中的数据），则必须手动处理所有id。一个很好的模式是这样的：

```
@Override

protected void buildModels(){

    new HeaderModel_()

        id("header")

        .title(..)

        .subtitle(...)

        .addTo(this);



    ...



    new LoaderModel_()

        .id("loader")

        .addIf(isLoading);

}
```

这与@AutoModel基本上具有相同的作用，但总的来说，我们更喜欢使用AutoModel，因为我们不必担心id冲突或分配唯一的id。在不嵌套的简单Controller中，您可能仍然喜欢使用这种方法。

### 与Kotlin搭配使用

如果您使用Kotlin编写EpoxyController类，则Epoxy可以生成用于构建Model的扩展方法。这将替换AutoModel模式。

对于名为 `HeaderViewModel` 的模型，用法如下所示。

```
override fun buildModels() {

  headerView {

    id("header")

    title("Hello World")

  }

}
```

要使用，请确保已在 `build.gradle` 文件中应用了kapt插件（`apply plugin: 'kotlin-kapt'`）。然后，Epoxy将在每个包含生成的EpoxyModels的包中生成一个名为`EpoxyModelKotlinExtensions.kt` 的Kotlin文件。对于该包中的每个生成的模型，此文件将在EpoxyController类上包含扩展方法。函数名称是删除了任何 `Epoxy` 或 `Model` 后缀的模型名称。

这些函数只能在Controller的 `buildModels` 方法中使用。每个函数都将带有Model的lambda作为接收器，创建Model的新实例，调用lambda以允许您初始化Model，然后将Model添加到Controller中。

您必须为以此方式构建的每个Model手动指定一个ID。

### 类型控制器

普通的EpoxyController不会规定用于构建其Model的数据来自何处。这使其具有灵活性，但在传递和存储数据时也可能需要额外的开销，或者会导致不良的架构模式。

`TypedEpoxyController` 旨在通过删除数据流周围的样板并鼓励buildModels为纯函数来解决此问题。为 `TypedEpoxyController` 的子类分配了数据类型，并在应重建模型时调用 `setData` 以传递该类型的对象。最后，使用该数据对象调用 `buildModels` 方法。

继续前面几节中的照片控制器示例，我们可以使用类型化的控制器（但没有加载器）来大大简化它：

```
class PhotoController extends TypedEpoxyController<List<Photo>> {

   @AutoModel HeaderModel_ header;



    @Override

    protected void buildModels(List<Photo> photos) {

      header

          .title("My Photos")

          .addTo(this);



      for (Photo photo : photos) {

        new PhotoModel_()

              .id(photo.getId())

              .url(photo.getUrl())

              .comment(photo.getComment())

              .addTo(this);

      }

    }

}
```

要使用此功能，只要照片数据发生更改并且我们希望重建模型，就调用 `photoController.setData(photos)` 。理想情况下， `buildModels(List<Photo>)` 实现的编写方式应仅取决于输入的 `photos` ，并且除了添加模型外没有其他副作用。这样，它是非常可预测的，可读的和可测试的。

`setData` 方法替换了基本EpoxyController上使用的常规 `requestModelBuild` 方法。直接在TypedController上调用 `requestModelBuild` 是错误的。

`Typed2EpoxyController` ，`Typed3EpoxyController` 和 `Typed4EpoxyController` 也可用-每个类型都有不同数量的类型参数。如果您的数据由多个对象表示，则这些功能很有用。

我们可以使用它来将加载数据恢复到我们的照片控制器：

```
class PhotoController extends Typed2EpoxyController<List<Photo>, Boolean> {

   @AutoModel HeaderModel_ header;

   @AutoModel LoadingModel_ loader;

    @Override

    protected void buildModels(List<Photo> photos, Boolean loadingMore) {

      header

          .title("My Photos")

          .addTo(this);



      for (Photo photo : photos) {

        new PhotoModel_()

              .id(photo.getId())

              .url(photo.getUrl())

              .comment(photo.getComment())

              .addTo(this);

      }



      loader

          .addIf(loadingMore, this);

    }

}
```

在这种情况下，我们可以调用 `photoController.setData(photos, isLoadingMore)` 使状态包括是否正在加载更多照片。不幸的是，原语是不允许的类型，因此我们必须使用 `Boolean` 而不是`boolean` 。另一个缺点是，默认情况下，setData的方法签名显示不清楚的通用参数名称（data1，data2等）。

为了解决这个问题，我们可以重写setData以提供清晰名称。

```
@Overridepublic void setData(List<Photo> photos, Boolean loadingMore) {

    super.setData(photos, Predicates.notNull(loadingMore));

}
```

这将为调用者提供有关 `Boolean` 参数表示什么的信息，并防止使用null值。

### 调试日志

调用 `setDebugLoggingEnabled(true)` 以启用将调试消息打印到Logcat。标签将是您的控制器类的名称。

这些调试消息将包括：

- **Diff结果**，检测到并通知了哪些item更改。选中此项对于验证您的数据更改是否具有预期的RecyclerView的更新很有用。Diff可能表明您的Model状态设置不正确，Model ID出现问题或`buildModels` 实现中存在其他常规错误。
- **定时**输出以报告构建和Diff Model所花费的时间。这对于描述性能并留意性能的倒退或变慢很有用。

### 过滤重复项

默认情况下，`DiffUtil` 差异算法会忽略具有重复ID的Item，并且在这种情况下行为是未定义的，因为它会破坏EpoxyController正常工作所需的RecyclerView稳定ID合同。在复杂的控制器中，很难保证没有稳定的id回归，如果有，我们应该在产品中进行适当的恢复。

为此，请调用 `setFilterDuplicates(true)`。启用后，Epoxy将搜索在 `buildModels` 期间添加了重复ID的Model，并删除找到的所有重复项。如果找到具有相同ID的模型，则第一个模型将留在适配器中，然后删除后续的模型。对于每个删除的重复项，将调用 `onExceptionSwallowed`。

如果通过服务器提供的数据创建Model，这可能很有用，在这种情况下，服务器可能会错误地发送重复项。或者，如果您的Model ID依赖于哈希（例如，任何字符串ID或多个数字ID），则您可能希望避免（极少数）哈希冲突。

### 拦截器

通常，将EpoxyModels在 `buildModels` 中添加到Controller后，无法对其进行修改。一个例外是如果使用了拦截器。拦截器在调用 `buildModels` 之后但在对Model进行Diff和在适配器上设置之前运行。那时，每个拦截器的 `intercept` 方法都将在 `buildModels` 期间添加的Model列表中调用，并且拦截器可以自由地修改列表以及列表中的每个Model。

这对于必须对模型进行合计操作的情况（例如切换分隔线）很有用。另一个有用的情况是修改A / B实验的模型。

使用 `addInterceptor` 添加拦截器。拦截器按照添加的顺序运行，每个后续拦截器接收由先前拦截器生成的修改后的列表。

`intercept` 方法return后，拦截器修改列表或列表中的任何模型都是错误的。

## Swallowed异常

如果Epoxy遇到可恢复的错误，它将调用 `onExceptionSwallowed` 并引发异常，然后尽可能继续进行。常见的情况是启用 `setFilterDuplicates` 时发现重复项。



默认情况下，`onExceptionSwallowed` 不执行任何操作，但是您的子类可以覆盖它以提醒您任何问题。一个好的模式是在您的app中拥有一个基本的EpoxyController，其他所有控制器都将其扩展。在基本控制器中，您可以重写onExceptionSwallowed并投入调试版本或登录生产环境中的崩溃报告服务。建议您不要忽略 `onExceptionSwallowed` ，因为它会报告Controller中的实际问题。
